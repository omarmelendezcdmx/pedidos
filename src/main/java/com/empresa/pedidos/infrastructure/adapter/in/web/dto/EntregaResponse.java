package com.empresa.pedidos.infrastructure.adapter.in.web.dto;

/**
 * Representación HTTP de una entrega.
 *
 * @param id             identificador
 * @param userId         cliente
 * @param orderRef       pedido asociado
 * @param direccionEnvio dirección de envío
 */
public record EntregaResponse(String id, String userId, String orderRef, String direccionEnvio) {
}
