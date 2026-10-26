package com.empresa.pedidos.domain.busqueda;

import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio que aplica los criterios de un {@link FiltroPedidos} sobre pedidos.
 */
public class BuscadorPedidos {

    /**
     * Busca pedidos que cumplan todos los filtros informados.
     *
     * @param pedidos universo de pedidos
     * @param filtro  filtros del usuario
     * @param limite  máximo de resultados
     * @return resultados ordenados por relevancia y número de pedido
     */
    public List<ResultadoBusqueda> buscar(Collection<Pedido> pedidos, FiltroPedidos filtro, int limite) {
        List<CriterioBusqueda> criterios = filtro.criterios();
        List<ResultadoBusqueda> resultados = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            evaluar(pedido, filtro, criterios).ifPresent(resultados::add);
        }
        return resultados.stream()
                .sorted(Comparator.comparingInt(ResultadoBusqueda::relevancia)
                        .thenComparing(resultado -> resultado.pedido().orderRef()))
                .limit(limite)
                .toList();
    }

    private Optional<ResultadoBusqueda> evaluar(
            Pedido pedido, FiltroPedidos filtro, List<CriterioBusqueda> criterios) {
        int relevancia = 0;
        for (CriterioBusqueda criterio : criterios) {
            int puntaje = criterio.evaluar(pedido);
            if (puntaje == CriterioBusqueda.NO_COINCIDE) {
                return Optional.empty();
            }
            relevancia += puntaje;
        }
        return Optional.of(new ResultadoBusqueda(pedido, itemsVisibles(pedido, filtro), relevancia));
    }

    private List<PedidoItem> itemsVisibles(Pedido pedido, FiltroPedidos filtro) {
        if (!filtro.filtraPorProducto()) {
            return pedido.items();
        }
        return pedido.items().stream()
                .filter(item -> Criterios.puntajeProducto(filtro.displayName(), item)
                        != CriterioBusqueda.NO_COINCIDE)
                .toList();
    }
}
