package com.empresa.pedidos.domain.model;

/**
 * Datos de entrega de un cliente, opcionalmente ligados a un pedido.
 *
 * @param id             identificador de la entrega (null si aún no se persiste)
 * @param userId         cliente al que pertenece
 * @param orderRef       pedido asociado, si aplica
 * @param direccionEnvio dirección de envío
 */
public record Entrega(String id, String userId, String orderRef, String direccionEnvio) {
}
