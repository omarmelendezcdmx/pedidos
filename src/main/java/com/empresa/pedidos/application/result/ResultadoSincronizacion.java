package com.empresa.pedidos.application.result;

import java.util.List;

/**
 * Resumen de una sincronización con los servicios externos.
 *
 * @param pedidosSincronizados  pedidos guardados
 * @param itemsRecibidos        items recibidos del servicio de items
 * @param itemsSinDetalle       items de pedidos que no existen en el servicio de items
 * @param clientesActualizados  clientes a los que se vincularon pedidos
 * @param userIdsSinCliente     usuarios de pedidos que aún no son clientes
 */
public record ResultadoSincronizacion(
        int pedidosSincronizados,
        int itemsRecibidos,
        int itemsSinDetalle,
        int clientesActualizados,
        List<String> userIdsSinCliente) {
}
