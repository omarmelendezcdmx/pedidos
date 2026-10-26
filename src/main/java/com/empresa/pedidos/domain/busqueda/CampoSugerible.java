package com.empresa.pedidos.domain.busqueda;

import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Campos que admiten autocompletado. Cada constante encapsula cómo extraer sus valores
 * de un pedido y si tolera errores de ortografía (patrón Strategy sobre enum).
 */
public enum CampoSugerible {

    /** Número de pedido. */
    ORDER_REF("orderRef", false, pedido -> Collections.singletonList(pedido.orderRef())),

    /** Fecha estimada de entrega. */
    ORDER_STATUS("orderStatus", false, pedido -> Collections.singletonList(pedido.orderStatus())),

    /** Nombre de la tienda. */
    STORE_NAME("storeName", true, pedido -> Collections.singletonList(pedido.storeName())),

    /** Nombre de los productos. */
    DISPLAY_NAME("displayName", true,
            pedido -> pedido.items().stream().map(PedidoItem::displayName).toList());

    private final String nombre;
    private final boolean toleraErrores;
    private final Function<Pedido, List<String>> extractor;

    CampoSugerible(String nombre, boolean toleraErrores, Function<Pedido, List<String>> extractor) {
        this.nombre = nombre;
        this.toleraErrores = toleraErrores;
        this.extractor = extractor;
    }

    /**
     * Resuelve un campo a partir de su nombre público.
     *
     * @param nombre orderRef, orderStatus, storeName o displayName
     * @return el campo correspondiente
     * @throws IllegalArgumentException si el nombre no corresponde a ningún campo
     */
    public static CampoSugerible desde(String nombre) {
        for (CampoSugerible campo : values()) {
            if (campo.nombre.equalsIgnoreCase(nombre)) {
                return campo;
            }
        }
        throw new IllegalArgumentException("campo debe ser orderRef, orderStatus, storeName o displayName");
    }

    /**
     * Indica si el campo tolera errores de ortografía al sugerir.
     *
     * @return true si tolera errores
     */
    public boolean toleraErrores() {
        return toleraErrores;
    }

    /**
     * Extrae los valores de este campo en un pedido.
     *
     * @param pedido pedido de origen
     * @return valores del campo, que pueden incluir nulos
     */
    public List<String> valoresDe(Pedido pedido) {
        return extractor.apply(pedido);
    }
}
