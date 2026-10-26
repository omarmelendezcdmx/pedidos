package com.empresa.pedidos.domain.model;

/**
 * Producto incluido en un pedido.
 *
 * @param itemId         identificador con formato orderRef-skuId
 * @param skuId          código del producto
 * @param displayName    nombre del producto (null si el servicio de items no lo conoce)
 * @param cantidad       unidades (null si se desconoce)
 * @param deliveryStatus estado de entrega del producto
 */
public record PedidoItem(
        String itemId,
        String skuId,
        String displayName,
        Integer cantidad,
        String deliveryStatus) {

    /**
     * Extrae el skuId de un itemId con formato orderRef-skuId.
     *
     * @param itemId identificador del item
     * @return la parte posterior al primer guion, o el mismo valor si no tiene guion
     */
    public static String skuDe(String itemId) {
        int separador = itemId == null ? -1 : itemId.indexOf('-');
        return separador >= 0 ? itemId.substring(separador + 1) : itemId;
    }

    /**
     * Crea un item conocido solo por su identificador, cuando no hay detalle disponible.
     *
     * @param itemId identificador del item
     * @return item sin nombre ni cantidad
     */
    public static PedidoItem sinDetalle(String itemId) {
        return new PedidoItem(itemId, skuDe(itemId), null, null, null);
    }
}
