package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import com.empresa.pedidos.domain.model.Cliente;
import com.empresa.pedidos.domain.model.Entrega;
import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import com.empresa.pedidos.infrastructure.adapter.out.persistence.PedidoDocument.ItemDocument;
import java.util.List;

/**
 * Traduce entre el modelo de dominio y los documentos MongoDB.
 */
final class PersistenceMapper {

    private PersistenceMapper() {
        // Clase de utilidad: no instanciable.
    }

    static Cliente aDominio(ClienteDocument doc) {
        return new Cliente(doc.userId(), doc.nombre(), doc.apellidoPaterno(), doc.apellidoMaterno(),
                doc.email(), doc.orders());
    }

    static ClienteDocument aDocumento(Cliente cliente) {
        return new ClienteDocument(cliente.userId(), cliente.nombre(), cliente.apellidoPaterno(),
                cliente.apellidoMaterno(), cliente.email(), cliente.orders());
    }

    static Entrega aDominio(EntregaDocument doc) {
        return new Entrega(doc.id(), doc.userId(), doc.orderRef(), doc.direccionEnvio());
    }

    static EntregaDocument aDocumento(Entrega entrega) {
        return new EntregaDocument(entrega.id(), entrega.userId(), entrega.orderRef(),
                entrega.direccionEnvio());
    }

    static Pedido aDominio(PedidoDocument doc) {
        List<PedidoItem> items = doc.items() == null ? List.of() : doc.items().stream()
                .map(item -> new PedidoItem(item.itemId(), item.skuId(), item.displayName(),
                        item.cantidad(), item.deliveryStatus()))
                .toList();
        return new Pedido(doc.orderRef(), doc.userId(), doc.canal(), doc.orderStatus(), doc.marketPlace(),
                doc.giftRegistry(), doc.storeName(), items);
    }

    static PedidoDocument aDocumento(Pedido pedido) {
        List<ItemDocument> items = pedido.items().stream()
                .map(item -> new ItemDocument(item.itemId(), item.skuId(), item.displayName(),
                        item.cantidad(), item.deliveryStatus()))
                .toList();
        return new PedidoDocument(pedido.orderRef(), pedido.userId(), pedido.canal(), pedido.orderStatus(),
                pedido.marketPlace(), pedido.giftRegistry(), pedido.storeName(), items);
    }
}
