package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import com.empresa.pedidos.application.port.out.EntregaRepositorio;
import com.empresa.pedidos.domain.model.Entrega;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida: implementa el puerto de entregas sobre MongoDB.
 */
@Component
public class EntregaMongoAdapter implements EntregaRepositorio {

    private final EntregaMongoRepository repositorio;

    EntregaMongoAdapter(EntregaMongoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Entrega guardar(Entrega entrega) {
        return PersistenceMapper.aDominio(repositorio.save(PersistenceMapper.aDocumento(entrega)));
    }

    @Override
    public Optional<Entrega> buscarPorId(String id) {
        return repositorio.findById(id).map(PersistenceMapper::aDominio);
    }

    @Override
    public List<Entrega> buscarTodas() {
        return repositorio.findAll().stream().map(PersistenceMapper::aDominio).toList();
    }

    @Override
    public List<Entrega> buscarPorUsuario(String userId) {
        return repositorio.findByUserId(userId).stream().map(PersistenceMapper::aDominio).toList();
    }

    @Override
    public void eliminar(String id) {
        repositorio.deleteById(id);
    }

    @Override
    public void eliminarPorUsuario(String userId) {
        repositorio.deleteByUserId(userId);
    }
}
