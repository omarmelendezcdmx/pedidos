package com.empresa.pedidos.infrastructure.adapter.out.external;

import com.empresa.pedidos.application.port.out.ItemExterno;
import com.empresa.pedidos.application.port.out.ItemsExternosPort;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida: consulta el servicio externo /items.
 */
@Component
public class ItemsHttpAdapter implements ItemsExternosPort {

    private final FuenteJson fuente;
    private final String url;

    /**
     * Crea el adaptador.
     *
     * @param fuente lector de JSON
     * @param url    URL completa del servicio /items
     */
    public ItemsHttpAdapter(FuenteJson fuente, @Value("${servicios.items.url}") String url) {
        this.fuente = fuente;
        this.url = url;
    }

    @Override
    public List<ItemExterno> obtenerItems() {
        return fuente.leerLista(url, ItemExternoDto.class).stream()
                .map(ItemExternoDto::aPuerto)
                .toList();
    }
}
