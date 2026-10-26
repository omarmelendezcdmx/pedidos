package com.empresa.pedidos.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento MongoDB de un cliente (colección clientes).
 *
 * @param userId          identificador
 * @param nombre          nombre de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param email           correo electrónico
 * @param orders          orderRef de los pedidos asociados
 */
@Document("clientes")
public record ClienteDocument(
        @Id String userId,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String email,
        List<String> orders) {
}
