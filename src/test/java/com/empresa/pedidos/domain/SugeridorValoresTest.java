package com.empresa.pedidos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.empresa.pedidos.domain.busqueda.CampoSugerible;
import com.empresa.pedidos.domain.busqueda.SugeridorValores;
import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.List;
import org.junit.jupiter.api.Test;

class SugeridorValoresTest {

    private final SugeridorValores sugeridor = new SugeridorValores();

    private final List<Pedido> pedidos = List.of(
            new Pedido("1", "u", "online", "2025-12-06", false, false, "L  SANTA FE",
                    List.of(new PedidoItem("1-a", "a", "Pantalón Levi´s", 1, null))),
            new Pedido("2", "u", "online", "2025-12-08", false, false, "Liverpool Santa Fe",
                    List.of(new PedidoItem("2-b", "b", "Playera cabellero Levi´s", 1, null),
                            new PedidoItem("2-c", "c", null, null, null))),
            new Pedido("3", "u", "online", "2025-12-06", false, false, null, List.of()));

    @Test
    void sugiereProductosSinDuplicadosYOrdenadosPorCoincidencia() {
        assertThat(sugeridor.sugerir(pedidos, CampoSugerible.DISPLAY_NAME, "levis", 10))
                .containsExactlyInAnyOrder("Pantalón Levi´s", "Playera cabellero Levi´s");
        assertThat(sugeridor.sugerir(pedidos, CampoSugerible.DISPLAY_NAME, "pant", 10))
                .containsExactly("Pantalón Levi´s");
    }

    @Test
    void agrupaFechasRepetidas() {
        assertThat(sugeridor.sugerir(pedidos, CampoSugerible.ORDER_STATUS, "2025-12", 10))
                .containsExactly("2025-12-06", "2025-12-08");
    }

    @Test
    void ignoraValoresNulosYRespetaElLimite() {
        assertThat(sugeridor.sugerir(pedidos, CampoSugerible.STORE_NAME, "", 1)).hasSize(1);
    }

    @Test
    void resuelveElCampoPorNombreYRechazaLosDesconocidos() {
        assertThat(CampoSugerible.desde("displayName")).isEqualTo(CampoSugerible.DISPLAY_NAME);
        assertThat(CampoSugerible.desde("ORDERREF")).isEqualTo(CampoSugerible.ORDER_REF);
        assertThatThrownBy(() -> CampoSugerible.desde("otro")).isInstanceOf(IllegalArgumentException.class);
    }
}
