package com.empresa.pedidos.application.command;

import java.util.List;

/**
 * Datos necesarios para crear o actualizar un pedido.
 *
 * @param orderRef     número de pedido; si es vacío al crear, se genera uno
 * @param userId       cliente
 * @param canal        canal de venta
 * @param orderStatus  fecha estimada de entrega (yyyy-MM-dd)
 * @param storeName    tienda
 * @param marketPlace  true si proviene de marketplace
 * @param giftRegistry true si pertenece a una mesa de regalos
 * @param items        productos del pedido
 */
public record PedidoCommand(
        String orderRef,
        String userId,
        String canal,
        String orderStatus,
        String storeName,
        boolean marketPlace,
        boolean giftRegistry,
        List<ItemCommand> items) {

    /**
     * Producto solicitado en un pedido.
     *
     * @param itemId      identificador del item
     * @param cantidad    unidades
     * @param displayName nombre del producto, opcional
     */
    public record ItemCommand(String itemId, Integer cantidad, String displayName) {
    }
}
