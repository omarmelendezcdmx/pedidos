package com.empresa.pedidos.application.port.in;

import com.empresa.pedidos.domain.busqueda.CampoSugerible;
import com.empresa.pedidos.domain.busqueda.FiltroPedidos;
import com.empresa.pedidos.domain.busqueda.ResultadoBusqueda;
import java.util.List;

/**
 * Puerto de entrada: búsqueda type-ahead de pedidos.
 */
public interface BusquedaUseCase {

    /**
     * Busca pedidos con los filtros indicados.
     *
     * @param filtro filtros del usuario
     * @param limite máximo de resultados
     * @return resultados ordenados por relevancia
     */
    List<ResultadoBusqueda> buscar(FiltroPedidos filtro, int limite);

    /**
     * Sugiere valores para autocompletar un campo.
     *
     * @param campo  campo a autocompletar
     * @param texto  texto escrito
     * @param limite máximo de sugerencias
     * @return sugerencias ordenadas
     */
    List<String> sugerir(CampoSugerible campo, String texto, int limite);
}
