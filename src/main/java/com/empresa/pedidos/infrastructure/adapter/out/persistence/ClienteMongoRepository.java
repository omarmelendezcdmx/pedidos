package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio Spring Data de clientes; uso interno del adaptador de persistencia.
 */
public interface ClienteMongoRepository extends MongoRepository<ClienteDocument, String> {

    /**
     * Busca por correo sin distinguir mayúsculas.
     *
     * @param email correo electrónico
     * @return el documento, si existe
     */
    Optional<ClienteDocument> findByEmailIgnoreCase(String email);
}
