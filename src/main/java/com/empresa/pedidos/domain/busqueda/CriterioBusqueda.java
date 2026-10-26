package com.empresa.pedidos.domain.busqueda;

import com.empresa.pedidos.domain.model.Pedido;

/**
 * Criterio de búsqueda sobre un pedido (patrón Strategy / Specification).
 *
 * Varios criterios se combinan con AND en {@link BuscadorPedidos}.
 */
@FunctionalInterface
public interface CriterioBusqueda {

    /** Puntaje que indica que el pedido no cumple el criterio. */
    int NO_COINCIDE = ComparadorTexto.NO_COINCIDE;

    /**
     * Evalúa el criterio.
     *
     * @param pedido pedido a evaluar
     * @return {@link #NO_COINCIDE} o un puntaje donde menor es mejor
     */
    int evaluar(Pedido pedido);
}
