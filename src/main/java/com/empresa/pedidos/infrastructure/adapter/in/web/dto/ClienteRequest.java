package com.empresa.pedidos.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Cuerpo HTTP para crear o actualizar un cliente.
 *
 * @param userId          identificador opcional; si se omite se genera uno
 * @param nombre          nombre de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param email           correo electrónico
 */
public record ClienteRequest(
        String userId,
        @NotBlank String nombre,
        @NotBlank String apellidoPaterno,
        String apellidoMaterno,
        @NotBlank @Email String email) {
}
