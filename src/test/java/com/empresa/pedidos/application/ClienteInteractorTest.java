package com.empresa.pedidos.application;

import static com.empresa.pedidos.application.DatosDePrueba.USER_ANA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.empresa.pedidos.application.command.ClienteCommand;
import com.empresa.pedidos.application.interactor.ClienteInteractor;
import com.empresa.pedidos.application.interactor.PedidoInteractor;
import com.empresa.pedidos.application.interactor.VinculadorPedidos;
import com.empresa.pedidos.domain.exception.ConflictoException;
import com.empresa.pedidos.domain.exception.RecursoNoEncontradoException;
import com.empresa.pedidos.domain.model.Cliente;
import com.empresa.pedidos.domain.model.Entrega;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClienteInteractorTest {

    private EnMemoria.Clientes clientes;
    private EnMemoria.Pedidos pedidos;
    private EnMemoria.Entregas entregas;
    private ClienteInteractor interactor;

    @BeforeEach
    void preparar() {
        clientes = new EnMemoria.Clientes();
        pedidos = new EnMemoria.Pedidos();
        entregas = new EnMemoria.Entregas();
        interactor = new ClienteInteractor(clientes, pedidos, entregas);
    }

    @Test
    void crearVinculaLosPedidosQueYaExistenConEseUserId() {
        new PedidoInteractor(pedidos, new VinculadorPedidos(clientes))
                .crear(DatosDePrueba.pedido("1001", USER_ANA));

        Cliente creado = interactor.crear(DatosDePrueba.clienteAna());

        assertThat(creado.userId()).isEqualTo(USER_ANA);
        assertThat(creado.orders()).containsExactly("1001");
    }

    @Test
    void crearSinUserIdGeneraUnoNuevo() {
        Cliente creado = interactor.crear(new ClienteCommand(null, "Ana", "López", null, "ana@correo.com"));

        assertThat(creado.userId()).isNotBlank();
    }

    @Test
    void crearConUserIdExistenteLanzaConflicto() {
        interactor.crear(DatosDePrueba.clienteAna());

        assertThatThrownBy(() -> interactor.crear(DatosDePrueba.clienteAna()))
                .isInstanceOf(ConflictoException.class);
    }

    @Test
    void crearConCorreoDuplicadoLanzaConflicto() {
        interactor.crear(DatosDePrueba.clienteAna());
        ClienteCommand otro = new ClienteCommand("otro", "Eva", "Gil", null, "ANA@correo.com");

        assertThatThrownBy(() -> interactor.crear(otro)).isInstanceOf(ConflictoException.class);
    }

    @Test
    void actualizarConservaElCampoOrders() {
        new PedidoInteractor(pedidos, new VinculadorPedidos(clientes))
                .crear(DatosDePrueba.pedido("1001", USER_ANA));
        interactor.crear(DatosDePrueba.clienteAna());

        Cliente actualizado = interactor.actualizar(USER_ANA,
                new ClienteCommand(USER_ANA, "Ana María", "López", "Ruiz", "ana@correo.com"));

        assertThat(actualizado.nombre()).isEqualTo("Ana María");
        assertThat(actualizado.orders()).containsExactly("1001");
    }

    @Test
    void obtenerInexistenteLanzaNoEncontrado() {
        assertThatThrownBy(() -> interactor.obtener("nadie"))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void eliminarBorraAlClienteYSusEntregas() {
        interactor.crear(DatosDePrueba.clienteAna());
        entregas.guardar(new Entrega(null, USER_ANA, null, "Av. Siempre Viva 742"));

        interactor.eliminar(USER_ANA);

        assertThat(clientes.existePorId(USER_ANA)).isFalse();
        assertThat(entregas.buscarPorUsuario(USER_ANA)).isEmpty();
    }
}
