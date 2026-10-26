package com.empresa.pedidos.infrastructure.adapter.in.web.dto;

import java.util.List;

/**
 * Representación HTTP de un pedido.
 *
 * @param orderRef     número de pedido
 * @param userId       cliente
 * @param canal        canal de venta
 * @param orderStatus  fecha estimada de entrega
 * @param marketPlace  indicador de marketplace
 * @param giftRegistry indicador de mesa de regalos
 * @param storeName    tienda
 * @param items        productos
 */
public record PedidoResponse(
        String orderRef,
        String userId,
        String canal,
        String orderStatus,
        boolean marketPlace,
        boolean giftRegistry,
        String storeName,
        List<ItemResponse> items) {

    /**
     * Representación HTTP de un producto de pedido.
     *
     * @param itemId         identificador
     * @param skuId          código del producto
     * @param displayName    nombre
     * @param cantidad       unidades
     * @param deliveryStatus estado de entrega
     */
    public record ItemResponse(
            String itemId,
            String skuId,
            String displayName,
            Integer cantidad,
            String deliveryStatus) {
    }
}
