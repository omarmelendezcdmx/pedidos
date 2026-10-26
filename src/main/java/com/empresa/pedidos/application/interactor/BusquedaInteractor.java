package com.empresa.pedidos.application.interactor;

import com.empresa.pedidos.application.port.in.BusquedaUseCase;
import com.empresa.pedidos.application.port.out.PedidoRepositorio;
import com.empresa.pedidos.domain.busqueda.BuscadorPedidos;
import com.empresa.pedidos.domain.busqueda.CampoSugerible;
import com.empresa.pedidos.domain.busqueda.FiltroPedidos;
import com.empresa.pedidos.domain.busqueda.ResultadoBusqueda;
import com.empresa.pedidos.domain.busqueda.SugeridorValores;
import java.util.List;

/**
 * Casos de uso de búsqueda: orquesta los servicios de dominio sobre los pedidos persistidos.
 */
public class BusquedaInteractor implements BusquedaUseCase {

    private final PedidoRepositorio pedidos;
    private final BuscadorPedidos buscador;
    private final SugeridorValores sugeridor;

    /**
     * Crea el interactor.
     *
     * @param pedidos   repositorio de pedidos
     * @param buscador  servicio de dominio de búsqueda
     * @param sugeridor servicio de dominio de autocompletado
     */
    public BusquedaInteractor(PedidoRepositorio pedidos, BuscadorPedidos buscador, SugeridorValores sugeridor) {
        this.pedidos = pedidos;
        this.buscador = buscador;
        this.sugeridor = sugeridor;
    }

    @Override
    public List<ResultadoBusqueda> buscar(FiltroPedidos filtro, int limite) {
        return buscador.buscar(pedidos.buscarTodos(), filtro, limite);
    }

    @Override
    public List<String> sugerir(CampoSugerible campo, String texto, int limite) {
        return sugeridor.sugerir(pedidos.buscarTodos(), campo, texto, limite);
    }
}
