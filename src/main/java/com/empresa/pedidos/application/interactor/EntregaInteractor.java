package com.empresa.pedidos.application.interactor;

import com.empresa.pedidos.application.command.EntregaCommand;
import com.empresa.pedidos.application.port.in.EntregaUseCase;
import com.empresa.pedidos.application.port.out.ClienteRepositorio;
import com.empresa.pedidos.application.port.out.EntregaRepositorio;
import com.empresa.pedidos.domain.exception.RecursoNoEncontradoException;
import com.empresa.pedidos.domain.model.Entrega;
import java.util.List;

/**
 * Casos de uso de datos de entrega.
 */
public class EntregaInteractor implements EntregaUseCase {

    private final EntregaRepositorio entregas;
    private final ClienteRepositorio clientes;

    /**
     * Crea el interactor.
     *
     * @param entregas repositorio de entregas
     * @param clientes repositorio de clientes
     */
    public EntregaInteractor(EntregaRepositorio entregas, ClienteRepositorio clientes) {
        this.entregas = entregas;
        this.clientes = clientes;
    }

    @Override
    public Entrega crear(EntregaCommand comando) {
        validarCliente(comando.userId());
        return entregas.guardar(new Entrega(null, comando.userId(), comando.orderRef(),
                comando.direccionEnvio()));
    }

    @Override
    public Entrega obtener(String id) {
        return entregas.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrega no encontrada: " + id));
    }

    @Override
    public List<Entrega> listar(String userId) {
        return userId == null || userId.isBlank() ? entregas.buscarTodas() : entregas.buscarPorUsuario(userId);
    }

    @Override
    public Entrega actualizar(String id, EntregaCommand comando) {
        obtener(id);
        validarCliente(comando.userId());
        return entregas.guardar(new Entrega(id, comando.userId(), comando.orderRef(),
                comando.direccionEnvio()));
    }

    @Override
    public void eliminar(String id) {
        obtener(id);
        entregas.eliminar(id);
    }

    private void validarCliente(String userId) {
        if (!clientes.existePorId(userId)) {
            throw new RecursoNoEncontradoException("Cliente no encontrado: " + userId);
        }
    }
}
