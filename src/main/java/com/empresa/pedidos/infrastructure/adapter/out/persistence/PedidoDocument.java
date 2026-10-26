package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento MongoDB de un pedido (colección pedidos).
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
@Document("pedidos")
public record PedidoDocument(
        @Id String orderRef,
        @Indexed String userId,
        String canal,
        String orderStatus,
        boolean marketPlace,
        boolean giftRegistry,
        String storeName,
        List<ItemDocument> items) {

    /**
     * Producto embebido en el documento de pedido.
     *
     * @param itemId         identificador
     * @param skuId          código del producto
     * @param displayName    nombre
     * @param cantidad       unidades
     * @param deliveryStatus estado de entrega
     */
    public record ItemDocument(
            String itemId,
            String skuId,
            String displayName,
            Integer cantidad,
            String deliveryStatus) {
    }
}
