package com.empresa.pedidos.domain.busqueda;

import com.empresa.pedidos.domain.model.Pedido;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio de dominio que propone valores de autocompletado a partir de los pedidos.
 */
public class SugeridorValores {

    /**
     * Sugiere valores distintos de un campo que coincidan con lo escrito.
     *
     * @param pedidos universo de pedidos
     * @param campo   campo a autocompletar
     * @param texto   lo que el usuario lleva escrito
     * @param limite  máximo de sugerencias
     * @return valores ordenados de mejor a peor coincidencia
     */
    public List<String> sugerir(Collection<Pedido> pedidos, CampoSugerible campo, String texto, int limite) {
        Map<String, Integer> candidatos = new LinkedHashMap<>();
        for (Pedido pedido : pedidos) {
            for (String valor : campo.valoresDe(pedido)) {
                evaluar(candidatos, texto, valor, campo.toleraErrores());
            }
        }
        return candidatos.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey)
                .limit(limite)
                .toList();
    }

    private void evaluar(Map<String, Integer> acumulado, String texto, String valor, boolean tolerar) {
        if (valor == null) {
            return;
        }
        int puntaje = ComparadorTexto.coincidencia(texto, valor, tolerar);
        if (puntaje != ComparadorTexto.NO_COINCIDE) {
            acumulado.merge(valor.trim().replaceAll("\\s+", " "), puntaje, Math::min);
        }
    }
}
