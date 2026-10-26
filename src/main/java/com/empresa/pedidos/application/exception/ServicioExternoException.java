package com.empresa.pedidos.application.exception;

/**
 * Se lanza cuando un servicio externo no responde o devuelve datos inválidos.
 */
public class ServicioExternoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción.
     *
     * @param mensaje descripción del fallo
     * @param causa   error original
     */
    public ServicioExternoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
