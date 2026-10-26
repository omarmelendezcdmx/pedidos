package com.empresa.pedidos.application.port.in;

import com.empresa.pedidos.application.command.PedidoCommand;
import com.empresa.pedidos.domain.model.Pedido;
import java.util.List;

/**
 * Puerto de entrada: gestión de pedidos.
 */
public interface PedidoUseCase {

    /**
     * Registra un pedido y lo asocia al cliente si existe.
     *
     * @param comando datos del pedido
     * @return pedido creado
     */
    Pedido crear(PedidoCommand comando);

    /**
     * Consulta un pedido.
     *
     * @param orderRef número de pedido
     * @return el pedido
     */
    Pedido obtener(String orderRef);

    /**
     * Lista pedidos, opcionalmente de un solo cliente.
     *
     * @param userId cliente, o null para todos
     * @return pedidos
     */
    List<Pedido> listar(String userId);

    /**
     * Actualiza un pedido y mantiene sincronizado el campo orders de los clientes.
     *
     * @param orderRef número de pedido
     * @param comando  nuevos datos
     * @return pedido actualizado
     */
    Pedido actualizar(String orderRef, PedidoCommand comando);

    /**
     * Elimina un pedido y lo desasocia de su cliente.
     *
     * @param orderRef número de pedido
     */
    void eliminar(String orderRef);
}
