package com.empresa.pedidos.domain.busqueda;

import java.util.ArrayList;
import java.util.List;

/**
 * Filtros solicitados por el usuario; los que vengan vacíos se ignoran.
 *
 * @param orderRef    prefijo del número de pedido
 * @param orderStatus prefijo de la fecha estimada de entrega
 * @param storeName   texto del nombre de la tienda
 * @param displayName texto del nombre de un producto
 * @param texto       texto libre sobre todos los campos
 */
public record FiltroPedidos(
        String orderRef,
        String orderStatus,
        String storeName,
        String displayName,
        String texto) {

    /**
     * Convierte los filtros informados en criterios combinables.
     *
     * @return lista de criterios, vacía si no se informó ningún filtro
     */
    public List<CriterioBusqueda> criterios() {
        List<CriterioBusqueda> criterios = new ArrayList<>();
        if (hay(orderRef)) {
            criterios.add(Criterios.porOrderRef(orderRef));
        }
        if (hay(orderStatus)) {
            criterios.add(Criterios.porFechaEstimada(orderStatus));
        }
        if (hay(storeName)) {
            criterios.add(Criterios.porTienda(storeName));
        }
        if (filtraPorProducto()) {
            criterios.add(Criterios.porProducto(displayName));
        }
        if (hay(texto)) {
            criterios.add(Criterios.porTextoLibre(texto));
        }
        return criterios;
    }

    /**
     * Indica si se filtra por nombre de producto.
     *
     * @return true si displayName fue informado
     */
    public boolean filtraPorProducto() {
        return hay(displayName);
    }

    private static boolean hay(String valor) {
        return valor != null && !valor.isBlank();
    }
}
