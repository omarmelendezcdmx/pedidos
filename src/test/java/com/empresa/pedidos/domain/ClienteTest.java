package com.empresa.pedidos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.empresa.pedidos.domain.model.Cliente;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.List;
import org.junit.jupiter.api.Test;

class ClienteTest {

    private final Cliente cliente = new Cliente("u1", "Ana", "López", "Ruiz", "ana@correo.com", null);

    @Test
    void ordersNuncaEsNulo() {
        assertThat(cliente.orders()).isEmpty();
    }

    @Test
    void conPedidosAgregaSinDuplicarYNoMutaElOriginal() {
        Cliente con = cliente.conPedidos(List.of("1", "2")).conPedidos(List.of("2", "3"));

        assertThat(con.orders()).containsExactly("1", "2", "3");
        assertThat(cliente.orders()).isEmpty();
    }

    @Test
    void sinPedidoQuitaSoloEsePedido() {
        Cliente sin = cliente.conPedidos(List.of("1", "2")).sinPedido("1");

        assertThat(sin.orders()).containsExactly("2");
    }

    @Test
    void conDatosConservaIdYPedidos() {
        Cliente nuevo = cliente.conPedidos(List.of("1")).conDatos("Eva", "Gil", null, "eva@correo.com");

        assertThat(nuevo.userId()).isEqualTo("u1");
        assertThat(nuevo.orders()).containsExactly("1");
        assertThat(nuevo.nombre()).isEqualTo("Eva");
    }

    @Test
    void skuDeExtraeLaParteDespuesDelPrimerGuion() {
        assertThat(PedidoItem.skuDe("3010091676-1132351437")).isEqualTo("1132351437");
        assertThat(PedidoItem.skuDe("sin-guion-extra")).isEqualTo("guion-extra");
        assertThat(PedidoItem.skuDe("sinseparador")).isEqualTo("sinseparador");
        assertThat(PedidoItem.skuDe(null)).isNull();
    }
}
