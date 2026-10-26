package com.empresa.pedidos.application.port.out;

import com.empresa.pedidos.domain.model.Pedido;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: persistencia de pedidos.
 */
public interface PedidoRepositorio {

    /**
     * Guarda o reemplaza un pedido (upsert por orderRef).
     *
     * @param pedido pedido a guardar
     * @return pedido persistido
     */
    Pedido guardar(Pedido pedido);

    /**
     * Guarda o reemplaza varios pedidos.
     *
     * @param pedidos pedidos a guardar
     */
    void guardarTodos(Collection<Pedido> pedidos);

    /**
     * Busca un pedido por número.
     *
     * @param orderRef número de pedido
     * @return el pedido, si existe
     */
    Optional<Pedido> buscarPorId(String orderRef);

    /**
     * Indica si existe un pedido con ese número.
     *
     * @param orderRef número de pedido
     * @return true si existe
     */
    boolean existePorId(String orderRef);

    /**
     * Lista todos los pedidos.
     *
     * @return pedidos registrados
     */
    List<Pedido> buscarTodos();

    /**
     * Lista los pedidos de un cliente.
     *
     * @param userId cliente
     * @return pedidos del cliente
     */
    List<Pedido> buscarPorUsuario(String userId);

    /**
     * Elimina un pedido.
     *
     * @param orderRef número de pedido
     */
    void eliminar(String orderRef);
}
