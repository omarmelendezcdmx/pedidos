package com.empresa.pedidos.application.port.out;

import java.util.List;

/**
 * Pedido tal como lo entrega el servicio externo /pedidos.
 *
 * @param orderRef     número de pedido
 * @param userId       usuario que lo realizó
 * @param canal        canal de venta
 * @param orderStatus  fecha estimada de entrega
 * @param marketPlace  indicador de marketplace
 * @param giftRegistry indicador de mesa de regalos
 * @param items        itemId de los productos
 * @param storeName    tienda
 */
public record PedidoExterno(
        String orderRef,
        String userId,
        String canal,
        String orderStatus,
        Boolean marketPlace,
        Boolean giftRegistry,
        List<String> items,
        String storeName) {
}
