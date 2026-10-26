package com.empresa.pedidos.domain.model;

import java.util.List;

/**
 * Pedido realizado desde la aplicación o el sitio web.
 *
 * @param orderRef    número de pedido
 * @param userId      cliente que lo realizó
 * @param canal       canal de venta (online, physical)
 * @param orderStatus estatus del pedido, expresado como fecha estimada de entrega (yyyy-MM-dd)
 * @param marketPlace indica si proviene de marketplace
 * @param giftRegistry indica si pertenece a una mesa de regalos
 * @param storeName   tienda
 * @param items       productos del pedido
 */
public record Pedido(
        String orderRef,
        String userId,
        String canal,
        String orderStatus,
        boolean marketPlace,
        boolean giftRegistry,
        String storeName,
        List<PedidoItem> items) {

    /**
     * Garantiza que la lista de productos sea inmutable y nunca nula.
     */
    public Pedido {
        items = items == null ? List.of() : List.copyOf(items);
    }
}
