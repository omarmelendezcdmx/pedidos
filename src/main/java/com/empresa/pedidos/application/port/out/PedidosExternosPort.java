package com.empresa.pedidos.application.port.out;

import java.util.List;

/**
 * Puerto de salida: consulta del servicio externo de pedidos.
 */
@FunctionalInterface
public interface PedidosExternosPort {

    /**
     * Obtiene los pedidos publicados por el servicio externo.
     *
     * @return pedidos externos
     */
    List<PedidoExterno> obtenerPedidos();
}
