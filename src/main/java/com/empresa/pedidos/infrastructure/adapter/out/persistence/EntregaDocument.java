package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento MongoDB de una entrega (colección entregas).
 *
 * @param id             identificador
 * @param userId         cliente
 * @param orderRef       pedido asociado
 * @param direccionEnvio dirección de envío
 */
@Document("entregas")
public record EntregaDocument(
        @Id String id,
        @Indexed String userId,
        String orderRef,
        String direccionEnvio) {
}
