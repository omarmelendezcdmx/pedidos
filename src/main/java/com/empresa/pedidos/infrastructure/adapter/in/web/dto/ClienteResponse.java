package com.empresa.pedidos.infrastructure.adapter.in.web.dto;

import java.util.List;

/**
 * Representación HTTP de un cliente.
 *
 * @param userId          identificador
 * @param nombre          nombre de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param email           correo electrónico
 * @param orders          orderRef de los pedidos asociados
 */
public record ClienteResponse(
        String userId,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String email,
        List<String> orders) {
}
