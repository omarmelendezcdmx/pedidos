package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import com.empresa.pedidos.application.port.out.PedidoRepositorio;
import com.empresa.pedidos.domain.model.Pedido;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida: implementa el puerto de pedidos sobre MongoDB.
 */
@Component
public class PedidoMongoAdapter implements PedidoRepositorio {

    private final PedidoMongoRepository repositorio;

    PedidoMongoAdapter(PedidoMongoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        return PersistenceMapper.aDominio(repositorio.save(PersistenceMapper.aDocumento(pedido)));
    }

    @Override
    public void guardarTodos(Collection<Pedido> pedidos) {
        repositorio.saveAll(pedidos.stream().map(PersistenceMapper::aDocumento).toList());
    }

    @Override
    public Optional<Pedido> buscarPorId(String orderRef) {
        return repositorio.findById(orderRef).map(PersistenceMapper::aDominio);
    }

    @Override
    public boolean existePorId(String orderRef) {
        return repositorio.existsById(orderRef);
    }

    @Override
    public List<Pedido> buscarTodos() {
        return repositorio.findAll().stream().map(PersistenceMapper::aDominio).toList();
    }

    @Override
    public List<Pedido> buscarPorUsuario(String userId) {
        return repositorio.findByUserId(userId).stream().map(PersistenceMapper::aDominio).toList();
    }

    @Override
    public void eliminar(String orderRef) {
        repositorio.deleteById(orderRef);
    }
}
