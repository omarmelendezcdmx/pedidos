package com.empresa.pedidos.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.empresa.pedidos.application.interactor.BusquedaInteractor;
import com.empresa.pedidos.domain.busqueda.BuscadorPedidos;
import com.empresa.pedidos.domain.busqueda.CampoSugerible;
import com.empresa.pedidos.domain.busqueda.FiltroPedidos;
import com.empresa.pedidos.domain.busqueda.SugeridorValores;
import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.List;
import org.junit.jupiter.api.Test;

class BusquedaInteractorTest {

    @Test
    void usaLosPedidosDelRepositorioParaBuscarYSugerir() {
        EnMemoria.Pedidos repositorio = new EnMemoria.Pedidos();
        repositorio.guardar(new Pedido("1001", "u1", "online", "2025-12-06", false, false, "L  SANTA FE",
                List.of(new PedidoItem("1001-1", "1", "Pantalón Levi´s", 1, null))));
        BusquedaInteractor interactor =
                new BusquedaInteractor(repositorio, new BuscadorPedidos(), new SugeridorValores());

        var encontrados = interactor.buscar(new FiltroPedidos(null, null, null, "pantalon levis", null), 10);
        var sugerencias = interactor.sugerir(CampoSugerible.STORE_NAME, "santa", 5);

        assertThat(encontrados).hasSize(1);
        assertThat(sugerencias).containsExactly("L SANTA FE");
    }
}
