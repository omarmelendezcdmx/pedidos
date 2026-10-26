package com.empresa.pedidos.domain.exception;

/**
 * Se lanza cuando la operación viola una regla de unicidad (por ejemplo, un duplicado).
 */
public class ConflictoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción.
     *
     * @param mensaje descripción del conflicto
     */
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
