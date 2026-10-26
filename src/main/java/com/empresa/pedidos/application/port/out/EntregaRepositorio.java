package com.empresa.pedidos.application.port.out;

import com.empresa.pedidos.domain.model.Entrega;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: persistencia de datos de entrega.
 */
public interface EntregaRepositorio {

    /**
     * Guarda o reemplaza una entrega; si no tiene id, se le asigna uno.
     *
     * @param entrega entrega a guardar
     * @return entrega persistida
     */
    Entrega guardar(Entrega entrega);

    /**
     * Busca una entrega por id.
     *
     * @param id identificador
     * @return la entrega, si existe
     */
    Optional<Entrega> buscarPorId(String id);

    /**
     * Lista todas las entregas.
     *
     * @return entregas registradas
     */
    List<Entrega> buscarTodas();

    /**
     * Lista las entregas de un cliente.
     *
     * @param userId cliente
     * @return entregas del cliente
     */
    List<Entrega> buscarPorUsuario(String userId);

    /**
     * Elimina una entrega.
     *
     * @param id identificador
     */
    void eliminar(String id);

    /**
     * Elimina todas las entregas de un cliente.
     *
     * @param userId cliente
     */
    void eliminarPorUsuario(String userId);
}
