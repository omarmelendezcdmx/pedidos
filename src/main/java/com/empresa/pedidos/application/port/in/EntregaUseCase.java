package com.empresa.pedidos.application.port.in;

import com.empresa.pedidos.application.command.EntregaCommand;
import com.empresa.pedidos.domain.model.Entrega;
import java.util.List;

/**
 * Puerto de entrada: gestión de datos de entrega.
 */
public interface EntregaUseCase {

    /**
     * Registra una entrega; el cliente debe existir.
     *
     * @param comando datos de la entrega
     * @return entrega creada
     */
    Entrega crear(EntregaCommand comando);

    /**
     * Consulta una entrega.
     *
     * @param id identificador
     * @return la entrega
     */
    Entrega obtener(String id);

    /**
     * Lista entregas, opcionalmente de un solo cliente.
     *
     * @param userId cliente, o null para todas
     * @return entregas
     */
    List<Entrega> listar(String userId);

    /**
     * Actualiza una entrega.
     *
     * @param id      identificador
     * @param comando nuevos datos
     * @return entrega actualizada
     */
    Entrega actualizar(String id, EntregaCommand comando);

    /**
     * Elimina una entrega.
     *
     * @param id identificador
     */
    void eliminar(String id);
}
