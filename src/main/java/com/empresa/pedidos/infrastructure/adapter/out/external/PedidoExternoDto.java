package com.empresa.pedidos.infrastructure.adapter.out.external;

import com.empresa.pedidos.application.port.out.PedidoExterno;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Contrato JSON del servicio externo /pedidos.
 *
 * @param id           identificador interno del servicio (se ignora)
 * @param orderRef     número de pedido
 * @param userId       usuario
 * @param canal        canal de venta
 * @param orderStatus  fecha estimada de entrega
 * @param marketPlace  indicador de marketplace
 * @param giftRegistry indicador de mesa de regalos
 * @param items        itemId de los productos
 * @param storeName    tienda
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PedidoExternoDto(
        String id,
        String orderRef,
        String userId,
        String canal,
        String orderStatus,
        Boolean marketPlace,
        Boolean giftRegistry,
        List<String> items,
        String storeName) {

    /**
     * Convierte el DTO al objeto del puerto.
     *
     * @return pedido externo para la capa de aplicación
     */
    PedidoExterno aPuerto() {
        return new PedidoExterno(orderRef, userId, canal, orderStatus, marketPlace, giftRegistry, items,
                storeName);
    }
}
