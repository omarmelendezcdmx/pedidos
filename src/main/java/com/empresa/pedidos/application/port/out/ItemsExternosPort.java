package com.empresa.pedidos.application.port.out;

import java.util.List;

/**
 * Puerto de salida: consulta del servicio externo de items.
 */
@FunctionalInterface
public interface ItemsExternosPort {

    /**
     * Obtiene los items publicados por el servicio externo.
     *
     * @return items externos
     */
    List<ItemExterno> obtenerItems();
}
