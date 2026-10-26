package com.empresa.pedidos.application.interactor;

import com.empresa.pedidos.application.port.out.ClienteRepositorio;
import com.empresa.pedidos.domain.model.Cliente;
import java.util.Collection;

/**
 * Mantiene sincronizado el campo orders de los clientes con sus pedidos.
 */
public class VinculadorPedidos {

    private final ClienteRepositorio clientes;

    /**
     * Crea el vinculador.
     *
     * @param clientes repositorio de clientes
     */
    public VinculadorPedidos(ClienteRepositorio clientes) {
        this.clientes = clientes;
    }

    /**
     * Agrega pedidos al cliente si este existe.
     *
     * @param userId    cliente
     * @param orderRefs pedidos a asociar
     * @return true si el cliente existe
     */
    public boolean vincular(String userId, Collection<String> orderRefs) {
        return clientes.buscarPorId(userId).map(cliente -> {
            Cliente actualizado = cliente.conPedidos(orderRefs);
            if (!actualizado.equals(cliente)) {
                clientes.guardar(actualizado);
            }
            return true;
        }).orElse(false);
    }

    /**
     * Quita un pedido del cliente si este existe.
     *
     * @param userId   cliente
     * @param orderRef pedido a desasociar
     */
    public void desvincular(String userId, String orderRef) {
        clientes.buscarPorId(userId).ifPresent(cliente -> {
            Cliente actualizado = cliente.sinPedido(orderRef);
            if (!actualizado.equals(cliente)) {
                clientes.guardar(actualizado);
            }
        });
    }
}
