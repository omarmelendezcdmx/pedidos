package com.empresa.pedidos.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Cuerpo HTTP para crear o actualizar una entrega.
 *
 * @param userId         cliente dueño de la entrega
 * @param orderRef       pedido asociado, opcional
 * @param direccionEnvio dirección de envío
 */
public record EntregaRequest(
        @NotBlank String userId,
        String orderRef,
        @NotBlank String direccionEnvio) {
}
