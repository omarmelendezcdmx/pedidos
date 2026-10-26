package com.empresa.pedidos.application;

import static com.empresa.pedidos.application.DatosDePrueba.USER_ANA;
import static com.empresa.pedidos.application.DatosDePrueba.USER_LUIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.empresa.pedidos.application.interactor.ClienteInteractor;
import com.empresa.pedidos.application.interactor.PedidoInteractor;
import com.empresa.pedidos.application.interactor.VinculadorPedidos;
import com.empresa.pedidos.domain.exception.ConflictoException;
import com.empresa.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PedidoInteractorTest {

    private EnMemoria.Clientes clientes;
    private EnMemoria.Pedidos pedidos;
    private PedidoInteractor interactor;

    @BeforeEach
    void preparar() {
        clientes = new EnMemoria.Clientes();
        pedidos = new EnMemoria.Pedidos();
        interactor = new PedidoInteractor(pedidos, new VinculadorPedidos(clientes));
        ClienteInteractor altaClientes = new ClienteInteractor(clientes, pedidos, new EnMemoria.Entregas());
        altaClientes.crear(DatosDePrueba.clienteAna());
        altaClientes.crear(DatosDePrueba.clienteLuis());
    }

    @Test
    void crearAgregaElOrderRefAlClienteYCalculaElSku() {
        Pedido creado = interactor.crear(DatosDePrueba.pedido("1001", USER_ANA));

        assertThat(clientes.buscarPorId(USER_ANA).orElseThrow().orders()).containsExactly("1001");
        assertThat(creado.items().get(0).skuId()).isEqualTo("111");
    }

    @Test
    void crearConOrderRefExistenteLanzaConflicto() {
        interactor.crear(DatosDePrueba.pedido("1001", USER_ANA));

        assertThatThrownBy(() -> interactor.crear(DatosDePrueba.pedido("1001", USER_ANA)))
                .isInstanceOf(ConflictoException.class);
    }

    @Test
    void actualizarConOtroUsuarioMueveElPedidoEntreClientes() {
        interactor.crear(DatosDePrueba.pedido("1001", USER_ANA));

        interactor.actualizar("1001", DatosDePrueba.pedido("1001", USER_LUIS));

        assertThat(clientes.buscarPorId(USER_ANA).orElseThrow().orders()).isEmpty();
        assertThat(clientes.buscarPorId(USER_LUIS).orElseThrow().orders()).containsExactly("1001");
    }

    @Test
    void eliminarQuitaElPedidoDelCliente() {
        interactor.crear(DatosDePrueba.pedido("1001", USER_ANA));

        interactor.eliminar("1001");

        assertThat(pedidos.existePorId("1001")).isFalse();
        assertThat(clientes.buscarPorId(USER_ANA).orElseThrow().orders()).isEmpty();
    }

    @Test
    void listarPorUsuarioFiltraSusPedidos() {
        interactor.crear(DatosDePrueba.pedido("1001", USER_ANA));
        interactor.crear(DatosDePrueba.pedido("1002", USER_LUIS));

        assertThat(interactor.listar(USER_ANA)).extracting(Pedido::orderRef).containsExactly("1001");
        assertThat(interactor.listar(null)).hasSize(2);
    }
}
