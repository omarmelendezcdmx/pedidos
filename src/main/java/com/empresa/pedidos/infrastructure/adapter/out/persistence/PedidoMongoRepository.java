package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio Spring Data de pedidos; uso interno del adaptador de persistencia.
 */
public interface PedidoMongoRepository extends MongoRepository<PedidoDocument, String> {

    /**
     * Lista los pedidos de un cliente.
     *
     * @param userId cliente
     * @return documentos de pedido
     */
    List<PedidoDocument> findByUserId(String userId);
}
