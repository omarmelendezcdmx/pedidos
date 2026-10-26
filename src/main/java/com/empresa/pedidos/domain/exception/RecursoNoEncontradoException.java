package com.empresa.pedidos.domain.exception;

/**
 * Se lanza cuando un recurso solicitado no existe.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción.
     *
     * @param mensaje descripción del recurso faltante
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
