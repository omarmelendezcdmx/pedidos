package com.empresa.pedidos.infrastructure.adapter.out.external;

import com.empresa.pedidos.application.port.out.ItemExterno;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Contrato JSON del servicio externo /items.
 *
 * @param id             identificador interno del servicio (se ignora)
 * @param itemId         identificador con formato orderRef-skuId
 * @param skuId          código del producto
 * @param quantity       cantidad
 * @param displayName    nombre del producto
 * @param deliveryStatus estado de entrega
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemExternoDto(
        String id,
        String itemId,
        String skuId,
        Integer quantity,
        String displayName,
        String deliveryStatus) {

    /**
     * Convierte el DTO al objeto del puerto.
     *
     * @return item externo para la capa de aplicación
     */
    ItemExterno aPuerto() {
        return new ItemExterno(itemId, skuId, quantity, displayName, deliveryStatus);
    }
}
