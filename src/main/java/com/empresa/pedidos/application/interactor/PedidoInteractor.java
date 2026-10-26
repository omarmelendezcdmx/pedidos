package com.empresa.pedidos.application.interactor;

import com.empresa.pedidos.application.command.PedidoCommand;
import com.empresa.pedidos.application.port.in.PedidoUseCase;
import com.empresa.pedidos.application.port.out.PedidoRepositorio;
import com.empresa.pedidos.domain.exception.ConflictoException;
import com.empresa.pedidos.domain.exception.RecursoNoEncontradoException;
import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.List;

/**
 * Casos de uso de pedidos.
 */
public class PedidoInteractor implements PedidoUseCase {

    private final PedidoRepositorio pedidos;
    private final VinculadorPedidos vinculador;

    /**
     * Crea el interactor.
     *
     * @param pedidos    repositorio de pedidos
     * @param vinculador mantiene el campo orders de los clientes
     */
    public PedidoInteractor(PedidoRepositorio pedidos, VinculadorPedidos vinculador) {
        this.pedidos = pedidos;
        this.vinculador = vinculador;
    }

    @Override
    public Pedido crear(PedidoCommand comando) {
        String orderRef = comando.orderRef() == null || comando.orderRef().isBlank()
                ? String.valueOf(System.currentTimeMillis())
                : comando.orderRef();
        if (pedidos.existePorId(orderRef)) {
            throw new ConflictoException("Ya existe el pedido " + orderRef);
        }
        Pedido guardado = pedidos.guardar(aDominio(orderRef, comando));
        vinculador.vincular(guardado.userId(), List.of(orderRef));
        return guardado;
    }

    @Override
    public Pedido obtener(String orderRef) {
        return pedidos.buscarPorId(orderRef)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado: " + orderRef));
    }

    @Override
    public List<Pedido> listar(String userId) {
        return userId == null || userId.isBlank() ? pedidos.buscarTodos() : pedidos.buscarPorUsuario(userId);
    }

    @Override
    public Pedido actualizar(String orderRef, PedidoCommand comando) {
        Pedido anterior = obtener(orderRef);
        Pedido guardado = pedidos.guardar(aDominio(orderRef, comando));
        if (!anterior.userId().equals(guardado.userId())) {
            vinculador.desvincular(anterior.userId(), orderRef);
        }
        vinculador.vincular(guardado.userId(), List.of(orderRef));
        return guardado;
    }

    @Override
    public void eliminar(String orderRef) {
        Pedido pedido = obtener(orderRef);
        pedidos.eliminar(orderRef);
        vinculador.desvincular(pedido.userId(), orderRef);
    }

    private Pedido aDominio(String orderRef, PedidoCommand comando) {
        List<PedidoItem> items = comando.items().stream()
                .map(item -> new PedidoItem(item.itemId(), PedidoItem.skuDe(item.itemId()),
                        item.displayName(), item.cantidad(), null))
                .toList();
        return new Pedido(orderRef, comando.userId(), comando.canal(), comando.orderStatus(),
                comando.marketPlace(), comando.giftRegistry(), comando.storeName(), items);
    }
}
