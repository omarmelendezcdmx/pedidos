package com.empresa.pedidos.infrastructure.adapter.in.web;

import com.empresa.pedidos.application.exception.ServicioExternoException;
import com.empresa.pedidos.domain.exception.ConflictoException;
import com.empresa.pedidos.domain.exception.RecursoNoEncontradoException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones de la aplicación y del dominio a respuestas HTTP (RFC 7807).
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Recurso inexistente.
     *
     * @param e excepción de dominio
     * @return respuesta 404
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    ProblemDetail noEncontrado(RecursoNoEncontradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * Duplicado o violación de unicidad.
     *
     * @param e excepción de dominio
     * @return respuesta 409
     */
    @ExceptionHandler(ConflictoException.class)
    ProblemDetail conflicto(ConflictoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * Fallo al consultar un servicio externo.
     *
     * @param e excepción de aplicación
     * @return respuesta 502
     */
    @ExceptionHandler(ServicioExternoException.class)
    ProblemDetail servicioExterno(ServicioExternoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, e.getMessage());
    }

    /**
     * Parámetro inválido.
     *
     * @param e excepción de argumento
     * @return respuesta 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail argumentoInvalido(IllegalArgumentException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Cuerpo que no supera la validación.
     *
     * @param e excepción de validación
     * @return respuesta 400 con el detalle de los campos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacion(MethodArgumentNotValidException e) {
        String detalle = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detalle);
    }
}
