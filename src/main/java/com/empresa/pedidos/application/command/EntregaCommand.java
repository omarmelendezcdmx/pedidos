package com.empresa.pedidos.application.command;

/**
 * Datos necesarios para crear o actualizar una entrega.
 *
 * @param userId         cliente dueño de la entrega
 * @param orderRef       pedido asociado, opcional
 * @param direccionEnvio dirección de envío
 */
public record EntregaCommand(String userId, String orderRef, String direccionEnvio) {
}
