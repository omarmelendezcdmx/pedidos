package com.empresa.pedidos.application;

import static com.empresa.pedidos.application.DatosDePrueba.USER_ANA;
import static org.assertj.core.api.Assertions.assertThat;

import com.empresa.pedidos.application.interactor.ClienteInteractor;
import com.empresa.pedidos.application.interactor.SincronizacionInteractor;
import com.empresa.pedidos.application.interactor.VinculadorPedidos;
import com.empresa.pedidos.application.port.out.ItemExterno;
import com.empresa.pedidos.application.port.out.PedidoExterno;
import com.empresa.pedidos.application.result.ResultadoSincronizacion;
import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SincronizacionInteractorTest {

    private EnMemoria.Clientes clientes;
    private EnMemoria.Pedidos pedidos;
    private SincronizacionInteractor interactor;

    @BeforeEach
    void preparar() {
        clientes = new EnMemoria.Clientes();
        pedidos = new EnMemoria.Pedidos();
        List<PedidoExterno> externos = List.of(
                new PedidoExterno("1001", USER_ANA, "online", "2025-12-06", false, false,
                        List.of("1001-111", "1001-222"), "L SANTA FE"),
                new PedidoExterno("1002", USER_ANA, "physical", "2025-12-08", true, null,
                        List.of("1002-999"), "Monterrey Centro"),
                new PedidoExterno("1003", "sin-cliente", "online", "2025-12-09", false, false,
                        List.of(), "Toluca"));
        List<ItemExterno> items = List.of(
                new ItemExterno("1001-111", "111", 3, "Pantalón Levi´s", "Compra en línea"),
                new ItemExterno("1001-222", "222", 4, "Vasos cristal", "Compra en tienda"));
        interactor = new SincronizacionInteractor(() -> externos, () -> items, pedidos,
                new VinculadorPedidos(clientes));
    }

    @Test
    void cruzaLosItemsPorItemIdYGuardaLosPedidos() {
        interactor.sincronizar();

        Pedido pedido = pedidos.buscarPorId("1001").orElseThrow();
        assertThat(pedido.items()).extracting(PedidoItem::displayName).containsExactly("Pantalón Levi´s", "Vasos cristal");
        assertThat(pedido.items()).extracting(PedidoItem::cantidad).containsExactly(3, 4);
    }

    @Test
    void conservaLosItemsQueNoExistenEnElServicioDeItems() {
        ResultadoSincronizacion resultado = interactor.sincronizar();

        Pedido pedido = pedidos.buscarPorId("1002").orElseThrow();
        assertThat(pedido.items()).hasSize(1);
        assertThat(pedido.items().get(0).displayName()).isNull();
        assertThat(pedido.items().get(0).skuId()).isEqualTo("999");
        assertThat(resultado.itemsSinDetalle()).isEqualTo(1);
    }

    @Test
    void vinculaLosPedidosAlClienteExistenteYReportaLosUsuariosSinCliente() {
        new ClienteInteractor(clientes, pedidos, new EnMemoria.Entregas()).crear(DatosDePrueba.clienteAna());

        ResultadoSincronizacion resultado = interactor.sincronizar();

        assertThat(clientes.buscarPorId(USER_ANA).orElseThrow().orders()).containsExactly("1001", "1002");
        assertThat(resultado.pedidosSincronizados()).isEqualTo(3);
        assertThat(resultado.itemsRecibidos()).isEqualTo(2);
        assertThat(resultado.clientesActualizados()).isEqualTo(1);
        assertThat(resultado.userIdsSinCliente()).containsExactly("sin-cliente");
    }

    @Test
    void esIdempotente() {
        new ClienteInteractor(clientes, pedidos, new EnMemoria.Entregas()).crear(DatosDePrueba.clienteAna());

        interactor.sincronizar();
        interactor.sincronizar();

        assertThat(pedidos.buscarTodos()).hasSize(3);
        assertThat(clientes.buscarPorId(USER_ANA).orElseThrow().orders()).containsExactly("1001", "1002");
    }
}
