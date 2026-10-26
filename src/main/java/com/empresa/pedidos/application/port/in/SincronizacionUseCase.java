package com.empresa.pedidos.application.port.in;

import com.empresa.pedidos.application.result.ResultadoSincronizacion;

/**
 * Puerto de entrada: sincronización con los servicios externos.
 */
@FunctionalInterface
public interface SincronizacionUseCase {

    /**
     * Consulta los servicios externos, persiste los pedidos y vincula clientes.
     *
     * @return resumen de la sincronización
     */
    ResultadoSincronizacion sincronizar();
}
