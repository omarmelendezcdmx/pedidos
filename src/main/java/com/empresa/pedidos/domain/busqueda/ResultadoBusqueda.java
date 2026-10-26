package com.empresa.pedidos.domain.busqueda;

import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.List;

/**
 * Pedido encontrado junto con los items que corresponde mostrar.
 *
 * @param pedido     pedido encontrado
 * @param items      todos sus items, o solo los que coinciden si se filtró por producto
 * @param relevancia puntaje total; menor es mejor
 */
public record ResultadoBusqueda(Pedido pedido, List<PedidoItem> items, int relevancia) {
}
