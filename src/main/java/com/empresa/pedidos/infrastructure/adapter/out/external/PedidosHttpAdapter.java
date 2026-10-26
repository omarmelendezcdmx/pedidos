package com.empresa.pedidos.infrastructure.adapter.out.external;

import com.empresa.pedidos.application.port.out.PedidoExterno;
import com.empresa.pedidos.application.port.out.PedidosExternosPort;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida: consulta el servicio externo /pedidos.
 */
@Component
public class PedidosHttpAdapter implements PedidosExternosPort {

    private final FuenteJson fuente;
    private final String url;

    /**
     * Crea el adaptador.
     *
     * @param fuente lector de JSON
     * @param url    URL completa del servicio /pedidos
     */
    public PedidosHttpAdapter(FuenteJson fuente, @Value("${servicios.pedidos.url}") String url) {
        this.fuente = fuente;
        this.url = url;
    }

    @Override
    public List<PedidoExterno> obtenerPedidos() {
        return fuente.leerLista(url, PedidoExternoDto.class).stream()
                .map(PedidoExternoDto::aPuerto)
                .toList();
    }
}
