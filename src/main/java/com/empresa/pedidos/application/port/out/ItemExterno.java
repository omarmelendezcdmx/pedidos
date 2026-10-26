package com.empresa.pedidos.application.port.out;

/**
 * Item tal como lo entrega el servicio externo /items.
 *
 * @param itemId         identificador con formato orderRef-skuId
 * @param skuId          código del producto
 * @param quantity       cantidad
 * @param displayName    nombre del producto
 * @param deliveryStatus estado de entrega
 */
public record ItemExterno(
        String itemId,
        String skuId,
        Integer quantity,
        String displayName,
        String deliveryStatus) {
}
