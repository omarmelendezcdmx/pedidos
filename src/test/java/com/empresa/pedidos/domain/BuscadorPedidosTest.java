package com.empresa.pedidos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.empresa.pedidos.domain.busqueda.BuscadorPedidos;
import com.empresa.pedidos.domain.busqueda.FiltroPedidos;
import com.empresa.pedidos.domain.busqueda.ResultadoBusqueda;
import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.List;
import org.junit.jupiter.api.Test;

class BuscadorPedidosTest {

    private final BuscadorPedidos buscador = new BuscadorPedidos();

    private final List<Pedido> pedidos = List.of(
            pedido("3010091676", "2025-12-06", "L  SANTA FE",
                    item("Pantalón Levi´s"), item("Vasos cristal 250ml")),
            pedido("30100916760987", "2025-12-06", "L  SANTA FE"),
            pedido("20251216366900020031", "2025-12-08", "Liverpool Galerías Toluca",
                    item("Control Xbox one"), item("Play Station 5")),
            pedido("632005897", "2026-05-15", "Monterrey Centro"));

    private static PedidoItem item(String nombre) {
        return new PedidoItem("x-" + nombre, "sku", nombre, 1, null);
    }

    private static Pedido pedido(String ref, String fecha, String tienda, PedidoItem... items) {
        return new Pedido(ref, "u", "online", fecha, false, false, tienda, List.of(items));
    }

    private List<String> refs(FiltroPedidos filtro) {
        return buscador.buscar(pedidos, filtro, 20).stream()
                .map(r -> r.pedido().orderRef())
                .toList();
    }

    @Test
    void sinFiltrosDevuelveTodoOrdenadoPorNumeroDePedido() {
        assertThat(refs(new FiltroPedidos(null, null, null, null, null)))
                .containsExactly("20251216366900020031", "3010091676", "30100916760987", "632005897");
    }

    @Test
    void orderRefCoincidePorPrefijo() {
        assertThat(refs(new FiltroPedidos("3010091676", null, null, null, null)))
                .containsExactly("3010091676", "30100916760987");
        assertThat(refs(new FiltroPedidos("63", null, null, null, null))).containsExactly("632005897");
    }

    @Test
    void orderStatusFiltraPorFecha() {
        assertThat(refs(new FiltroPedidos(null, "2025-12-08", null, null, null)))
                .containsExactly("20251216366900020031");
        assertThat(refs(new FiltroPedidos(null, "2026", null, null, null))).containsExactly("632005897");
    }

    @Test
    void storeNameIgnoraAcentosMayusculasYComas() {
        assertThat(refs(new FiltroPedidos(null, null, "GALERIAS, toluca", null, null)))
                .containsExactly("20251216366900020031");
    }

    @Test
    void displayNameDevuelveSoloLosItemsQueCoinciden() {
        List<ResultadoBusqueda> resultado =
                buscador.buscar(pedidos, new FiltroPedidos(null, null, null, "pantalon levis", null), 20);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).items()).extracting(PedidoItem::displayName).containsExactly("Pantalón Levi´s");
    }

    @Test
    void displayNameToleraErroresDeOrtografia() {
        assertThat(refs(new FiltroPedidos(null, null, null, "plei steishon", null))).isEmpty();
        assertThat(refs(new FiltroPedidos(null, null, null, "play statin", null)))
                .containsExactly("20251216366900020031");
    }

    @Test
    void losFiltrosSeCombinanConAnd() {
        assertThat(refs(new FiltroPedidos(null, "2025-12-06", "santa", "vasos", null)))
                .containsExactly("3010091676");
        assertThat(refs(new FiltroPedidos(null, "2026", "santa", null, null))).isEmpty();
    }

    @Test
    void textoLibreBuscaEnTodosLosCampos() {
        assertThat(refs(new FiltroPedidos(null, null, null, null, "monterrey"))).containsExactly("632005897");
        assertThat(refs(new FiltroPedidos(null, null, null, null, "xbox toluca")))
                .containsExactly("20251216366900020031");
    }

    @Test
    void respetaElLimite() {
        assertThat(buscador.buscar(pedidos, new FiltroPedidos(null, null, null, null, null), 2)).hasSize(2);
    }
}
