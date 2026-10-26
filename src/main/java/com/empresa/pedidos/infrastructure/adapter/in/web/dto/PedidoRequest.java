package com.empresa.pedidos.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

/**
 * Cuerpo HTTP para crear o actualizar un pedido.
 *
 * @param orderRef     número de pedido opcional; si se omite se genera uno
 * @param userId       cliente
 * @param canal        canal de venta
 * @param orderStatus  fecha estimada de entrega (yyyy-MM-dd)
 * @param storeName    tienda
 * @param marketPlace  true si proviene de marketplace
 * @param giftRegistry true si pertenece a una mesa de regalos
 * @param items        productos del pedido
 */
public record PedidoRequest(
        String orderRef,
        @NotBlank String userId,
        @NotBlank @Schema(example = "online") String canal,
        @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "debe tener formato yyyy-MM-dd")
        @Schema(description = "Fecha estimada de entrega", example = "2025-12-06") String orderStatus,
        String storeName,
        Boolean marketPlace,
        Boolean giftRegistry,
        @NotEmpty @Valid List<ItemRequest> items) {

    /**
     * Producto solicitado en el pedido.
     *
     * @param itemId      identificador del item
     * @param cantidad    unidades (mínimo 1)
     * @param displayName nombre del producto, opcional
     */
    public record ItemRequest(
            @NotBlank String itemId,
            @NotNull @Min(1) Integer cantidad,
            String displayName) {
    }
}
