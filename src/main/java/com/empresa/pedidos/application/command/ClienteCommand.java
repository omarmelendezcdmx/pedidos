package com.empresa.pedidos.application.command;

/**
 * Datos necesarios para crear o actualizar un cliente.
 *
 * @param userId          identificador; si es vacío al crear, se genera uno
 * @param nombre          nombre de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param email           correo electrónico
 */
public record ClienteCommand(
        String userId,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String email) {
}
