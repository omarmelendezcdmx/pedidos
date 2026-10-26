package com.empresa.pedidos.infrastructure.adapter.in.web.dto;

import java.util.List;

/**
 * Resumen HTTP de una sincronización.
 *
 * @param pedidosSincronizados pedidos guardados
 * @param itemsRecibidos       items recibidos del servicio de items
 * @param itemsSinDetalle      items de pedidos ausentes en el servicio de items
 * @param clientesActualizados clientes a los que se vincularon pedidos
 * @param userIdsSinCliente    usuarios de pedidos que aún no son clientes
 */
public record SincronizacionResponse(
        int pedidosSincronizados,
        int itemsRecibidos,
        int itemsSinDetalle,
        int clientesActualizados,
        List<String> userIdsSinCliente) {
}
