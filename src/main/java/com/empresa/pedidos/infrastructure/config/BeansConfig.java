package com.empresa.pedidos.infrastructure.config;

import com.empresa.pedidos.application.interactor.BusquedaInteractor;
import com.empresa.pedidos.application.interactor.ClienteInteractor;
import com.empresa.pedidos.application.interactor.EntregaInteractor;
import com.empresa.pedidos.application.interactor.PedidoInteractor;
import com.empresa.pedidos.application.interactor.SincronizacionInteractor;
import com.empresa.pedidos.application.interactor.VinculadorPedidos;
import com.empresa.pedidos.application.port.in.BusquedaUseCase;
import com.empresa.pedidos.application.port.in.ClienteUseCase;
import com.empresa.pedidos.application.port.in.EntregaUseCase;
import com.empresa.pedidos.application.port.in.PedidoUseCase;
import com.empresa.pedidos.application.port.in.SincronizacionUseCase;
import com.empresa.pedidos.application.port.out.ClienteRepositorio;
import com.empresa.pedidos.application.port.out.EntregaRepositorio;
import com.empresa.pedidos.application.port.out.ItemsExternosPort;
import com.empresa.pedidos.application.port.out.PedidoRepositorio;
import com.empresa.pedidos.application.port.out.PedidosExternosPort;
import com.empresa.pedidos.domain.busqueda.BuscadorPedidos;
import com.empresa.pedidos.domain.busqueda.SugeridorValores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Raíz de composición: conecta los casos de uso con sus adaptadores (inyección de dependencias).
 *
 * La capa de aplicación y el dominio no usan anotaciones de Spring; se instancian aquí.
 */
@Configuration
public class BeansConfig {

    @Bean
    VinculadorPedidos vinculadorPedidos(ClienteRepositorio clientes) {
        return new VinculadorPedidos(clientes);
    }

    @Bean
    ClienteUseCase clienteUseCase(
            ClienteRepositorio clientes, PedidoRepositorio pedidos, EntregaRepositorio entregas) {
        return new ClienteInteractor(clientes, pedidos, entregas);
    }

    @Bean
    EntregaUseCase entregaUseCase(EntregaRepositorio entregas, ClienteRepositorio clientes) {
        return new EntregaInteractor(entregas, clientes);
    }

    @Bean
    PedidoUseCase pedidoUseCase(PedidoRepositorio pedidos, VinculadorPedidos vinculador) {
        return new PedidoInteractor(pedidos, vinculador);
    }

    @Bean
    SincronizacionUseCase sincronizacionUseCase(
            PedidosExternosPort pedidosExternos,
            ItemsExternosPort itemsExternos,
            PedidoRepositorio pedidos,
            VinculadorPedidos vinculador) {
        return new SincronizacionInteractor(pedidosExternos, itemsExternos, pedidos, vinculador);
    }

    @Bean
    BusquedaUseCase busquedaUseCase(PedidoRepositorio pedidos) {
        return new BusquedaInteractor(pedidos, new BuscadorPedidos(), new SugeridorValores());
    }
}
