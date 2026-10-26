package com.empresa.pedidos.application.port.in;

import com.empresa.pedidos.application.command.ClienteCommand;
import com.empresa.pedidos.domain.model.Cliente;
import com.empresa.pedidos.domain.model.Pedido;
import java.util.List;

/**
 * Puerto de entrada: gestión de clientes.
 */
public interface ClienteUseCase {

    /**
     * Registra un cliente y le vincula los pedidos que ya existan con su userId.
     *
     * @param comando datos del cliente
     * @return cliente creado
     */
    Cliente crear(ClienteCommand comando);

    /**
     * Consulta un cliente.
     *
     * @param userId identificador
     * @return el cliente
     */
    Cliente obtener(String userId);

    /**
     * Lista los clientes.
     *
     * @return clientes registrados
     */
    List<Cliente> listar();

    /**
     * Actualiza los datos de un cliente, conservando sus pedidos.
     *
     * @param userId  identificador
     * @param comando nuevos datos
     * @return cliente actualizado
     */
    Cliente actualizar(String userId, ClienteCommand comando);

    /**
     * Elimina un cliente y sus datos de entrega.
     *
     * @param userId identificador
     */
    void eliminar(String userId);

    /**
     * Consulta el detalle de los pedidos de un cliente.
     *
     * @param userId identificador
     * @return pedidos del cliente
     */
    List<Pedido> pedidosDe(String userId);
}
