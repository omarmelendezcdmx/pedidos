package com.empresa.pedidos.infrastructure.adapter.in.web.dto;

import com.empresa.pedidos.infrastructure.adapter.in.web.dto.PedidoResponse.ItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Pedido devuelto por la búsqueda.
 *
 * @param orderRef     número de pedido
 * @param userId       cliente
 * @param canal        canal de venta
 * @param orderStatus  fecha estimada de entrega
 * @param storeName    tienda
 * @param marketPlace  indicador de marketplace
 * @param giftRegistry indicador de mesa de regalos
 * @param items        productos; solo los que coinciden si se filtró por displayName
 * @param relevancia   menor valor indica mejor coincidencia
 */
public record BusquedaPedidoResponse(
        String orderRef,
        String userId,
        String canal,
        String orderStatus,
        String storeName,
        boolean marketPlace,
        boolean giftRegistry,
        @Schema(description = "Si se filtró por displayName, solo los items que coinciden") List<ItemResponse> items,
        @Schema(description = "Menor valor = mejor coincidencia") int relevancia) {
}
