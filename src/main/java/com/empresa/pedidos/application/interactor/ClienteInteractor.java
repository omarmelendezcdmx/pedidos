package com.empresa.pedidos.application.interactor;

import com.empresa.pedidos.application.command.ClienteCommand;
import com.empresa.pedidos.application.port.in.ClienteUseCase;
import com.empresa.pedidos.application.port.out.ClienteRepositorio;
import com.empresa.pedidos.application.port.out.EntregaRepositorio;
import com.empresa.pedidos.application.port.out.PedidoRepositorio;
import com.empresa.pedidos.domain.exception.ConflictoException;
import com.empresa.pedidos.domain.exception.RecursoNoEncontradoException;
import com.empresa.pedidos.domain.model.Cliente;
import com.empresa.pedidos.domain.model.Pedido;
import java.util.List;
import java.util.UUID;

/**
 * Casos de uso de clientes.
 */
public class ClienteInteractor implements ClienteUseCase {

    private final ClienteRepositorio clientes;
    private final PedidoRepositorio pedidos;
    private final EntregaRepositorio entregas;

    /**
     * Crea el interactor.
     *
     * @param clientes repositorio de clientes
     * @param pedidos  repositorio de pedidos
     * @param entregas repositorio de entregas
     */
    public ClienteInteractor(
            ClienteRepositorio clientes, PedidoRepositorio pedidos, EntregaRepositorio entregas) {
        this.clientes = clientes;
        this.pedidos = pedidos;
        this.entregas = entregas;
    }

    @Override
    public Cliente crear(ClienteCommand comando) {
        String userId = comando.userId() == null || comando.userId().isBlank()
                ? UUID.randomUUID().toString()
                : comando.userId();
        if (clientes.existePorId(userId)) {
            throw new ConflictoException("Ya existe un cliente con userId " + userId);
        }
        validarEmailUnico(comando.email(), null);
        List<String> orders = pedidos.buscarPorUsuario(userId).stream().map(Pedido::orderRef).toList();
        return clientes.guardar(new Cliente(userId, comando.nombre(), comando.apellidoPaterno(),
                comando.apellidoMaterno(), comando.email(), orders));
    }

    @Override
    public Cliente obtener(String userId) {
        return clientes.buscarPorId(userId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + userId));
    }

    @Override
    public List<Cliente> listar() {
        return clientes.buscarTodos();
    }

    @Override
    public Cliente actualizar(String userId, ClienteCommand comando) {
        Cliente actual = obtener(userId);
        validarEmailUnico(comando.email(), userId);
        return clientes.guardar(actual.conDatos(comando.nombre(), comando.apellidoPaterno(),
                comando.apellidoMaterno(), comando.email()));
    }

    @Override
    public void eliminar(String userId) {
        obtener(userId);
        entregas.eliminarPorUsuario(userId);
        clientes.eliminar(userId);
    }

    @Override
    public List<Pedido> pedidosDe(String userId) {
        obtener(userId);
        return pedidos.buscarPorUsuario(userId);
    }

    private void validarEmailUnico(String email, String userIdActual) {
        clientes.buscarPorEmail(email)
                .filter(otro -> !otro.userId().equals(userIdActual))
                .ifPresent(otro -> {
                    throw new ConflictoException("El correo ya está registrado: " + email);
                });
    }
}
