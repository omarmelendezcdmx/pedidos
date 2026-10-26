package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import com.empresa.pedidos.application.port.out.ClienteRepositorio;
import com.empresa.pedidos.domain.model.Cliente;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida: implementa el puerto de clientes sobre MongoDB.
 */
@Component
public class ClienteMongoAdapter implements ClienteRepositorio {

    private final ClienteMongoRepository repositorio;

    ClienteMongoAdapter(ClienteMongoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return PersistenceMapper.aDominio(repositorio.save(PersistenceMapper.aDocumento(cliente)));
    }

    @Override
    public Optional<Cliente> buscarPorId(String userId) {
        return repositorio.findById(userId).map(PersistenceMapper::aDominio);
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return repositorio.findByEmailIgnoreCase(email).map(PersistenceMapper::aDominio);
    }

    @Override
    public boolean existePorId(String userId) {
        return repositorio.existsById(userId);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return repositorio.findAll().stream().map(PersistenceMapper::aDominio).toList();
    }

    @Override
    public void eliminar(String userId) {
        repositorio.deleteById(userId);
    }
}
