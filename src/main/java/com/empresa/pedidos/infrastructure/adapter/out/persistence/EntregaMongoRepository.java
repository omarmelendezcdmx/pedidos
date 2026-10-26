package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio Spring Data de entregas; uso interno del adaptador de persistencia.
 */
public interface EntregaMongoRepository extends MongoRepository<EntregaDocument, String> {

    /**
     * Lista las entregas de un cliente.
     *
     * @param userId cliente
     * @return documentos de entrega
     */
    List<EntregaDocument> findByUserId(String userId);

    /**
     * Elimina las entregas de un cliente.
     *
     * @param userId cliente
     */
    void deleteByUserId(String userId);
}
