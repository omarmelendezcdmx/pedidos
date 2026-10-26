package com.empresa.pedidos.application.port.out;

import com.empresa.pedidos.domain.model.Cliente;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: persistencia de clientes.
 */
public interface ClienteRepositorio {

    /**
     * Guarda o reemplaza un cliente.
     *
     * @param cliente cliente a guardar
     * @return cliente persistido
     */
    Cliente guardar(Cliente cliente);

    /**
     * Busca un cliente por userId.
     *
     * @param userId identificador
     * @return el cliente, si existe
     */
    Optional<Cliente> buscarPorId(String userId);

    /**
     * Busca un cliente por correo, sin distinguir mayúsculas.
     *
     * @param email correo electrónico
     * @return el cliente, si existe
     */
    Optional<Cliente> buscarPorEmail(String email);

    /**
     * Indica si existe un cliente con ese userId.
     *
     * @param userId identificador
     * @return true si existe
     */
    boolean existePorId(String userId);

    /**
     * Lista todos los clientes.
     *
     * @return clientes registrados
     */
    List<Cliente> buscarTodos();

    /**
     * Elimina un cliente.
     *
     * @param userId identificador
     */
    void eliminar(String userId);
}
