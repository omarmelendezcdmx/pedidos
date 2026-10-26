package com.empresa.pedidos.domain.busqueda;

import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.Objects;

/**
 * Fábrica de criterios de búsqueda (patrón Factory Method).
 */
public final class Criterios {

    private Criterios() {
        // Clase de utilidad: no instanciable.
    }

    /**
     * Criterio por número de pedido, solo por prefijo.
     *
     * @param consulta texto buscado
     * @return criterio sobre orderRef
     */
    public static CriterioBusqueda porOrderRef(String consulta) {
        return pedido -> ComparadorTexto.coincidencia(consulta, pedido.orderRef(), false);
    }

    /**
     * Criterio por fecha estimada de entrega (orderStatus), solo por prefijo.
     *
     * @param consulta texto buscado, por ejemplo 2025-12
     * @return criterio sobre orderStatus
     */
    public static CriterioBusqueda porFechaEstimada(String consulta) {
        return pedido -> ComparadorTexto.coincidencia(consulta, pedido.orderStatus(), false);
    }

    /**
     * Criterio por nombre de tienda, tolerante a errores.
     *
     * @param consulta texto buscado
     * @return criterio sobre storeName
     */
    public static CriterioBusqueda porTienda(String consulta) {
        return pedido -> ComparadorTexto.coincidencia(consulta, pedido.storeName(), true);
    }

    /**
     * Criterio por nombre de producto: basta con que un item coincida.
     *
     * @param consulta texto buscado
     * @return criterio sobre displayName de los items
     */
    public static CriterioBusqueda porProducto(String consulta) {
        return pedido -> pedido.items().stream()
                .mapToInt(item -> puntajeProducto(consulta, item))
                .filter(puntaje -> puntaje != CriterioBusqueda.NO_COINCIDE)
                .min()
                .orElse(CriterioBusqueda.NO_COINCIDE);
    }

    /**
     * Criterio de texto libre sobre pedido, fecha, tienda, canal y productos.
     *
     * @param consulta texto buscado
     * @return criterio sobre todos los campos
     */
    public static CriterioBusqueda porTextoLibre(String consulta) {
        return pedido -> ComparadorTexto.coincidencia(consulta, textoGlobal(pedido), true);
    }

    /**
     * Puntaje de un item frente a una consulta de producto.
     *
     * @param consulta texto buscado
     * @param item     item a evaluar
     * @return puntaje de coincidencia o {@link CriterioBusqueda#NO_COINCIDE}
     */
    public static int puntajeProducto(String consulta, PedidoItem item) {
        return ComparadorTexto.coincidencia(consulta, item.displayName(), true);
    }

    private static String textoGlobal(Pedido pedido) {
        StringBuilder texto = new StringBuilder();
        texto.append(pedido.orderRef()).append(' ')
                .append(pedido.orderStatus()).append(' ')
                .append(Objects.toString(pedido.storeName(), "")).append(' ')
                .append(Objects.toString(pedido.canal(), ""));
        pedido.items().forEach(item -> texto.append(' ').append(Objects.toString(item.displayName(), "")));
        return texto.toString();
    }
}
