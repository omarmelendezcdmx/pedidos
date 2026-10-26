package com.empresa.pedidos.application.interactor;

import com.empresa.pedidos.application.port.in.SincronizacionUseCase;
import com.empresa.pedidos.application.port.out.ItemExterno;
import com.empresa.pedidos.application.port.out.ItemsExternosPort;
import com.empresa.pedidos.application.port.out.PedidoExterno;
import com.empresa.pedidos.application.port.out.PedidoRepositorio;
import com.empresa.pedidos.application.port.out.PedidosExternosPort;
import com.empresa.pedidos.application.result.ResultadoSincronizacion;
import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consulta /pedidos e /items, cruza por itemId, persiste los pedidos y vincula
 * el orderRef de cada pedido con su cliente (campo orders).
 */
public class SincronizacionInteractor implements SincronizacionUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(SincronizacionInteractor.class);

    private final PedidosExternosPort pedidosExternos;
    private final ItemsExternosPort itemsExternos;
    private final PedidoRepositorio pedidos;
    private final VinculadorPedidos vinculador;

    /**
     * Crea el interactor.
     *
     * @param pedidosExternos puerto hacia el servicio /pedidos
     * @param itemsExternos   puerto hacia el servicio /items
     * @param pedidos         repositorio de pedidos
     * @param vinculador      mantiene el campo orders de los clientes
     */
    public SincronizacionInteractor(
            PedidosExternosPort pedidosExternos,
            ItemsExternosPort itemsExternos,
            PedidoRepositorio pedidos,
            VinculadorPedidos vinculador) {
        this.pedidosExternos = pedidosExternos;
        this.itemsExternos = itemsExternos;
        this.pedidos = pedidos;
        this.vinculador = vinculador;
    }

    @Override
    public ResultadoSincronizacion sincronizar() {
        List<PedidoExterno> externos = pedidosExternos.obtenerPedidos();
        List<ItemExterno> items = itemsExternos.obtenerItems();

        Map<String, ItemExterno> itemsPorId = new LinkedHashMap<>();
        items.forEach(item -> itemsPorId.putIfAbsent(item.itemId(), item));

        List<Pedido> pedidosAGuardar = new ArrayList<>();
        Map<String, List<String>> refsPorUsuario = new LinkedHashMap<>();
        int sinDetalle = 0;
        for (PedidoExterno externo : externos) {
            Armado armado = armar(externo, itemsPorId);
            pedidosAGuardar.add(armado.pedido());
            sinDetalle += armado.sinDetalle();
            if (externo.userId() != null && !externo.userId().isBlank()) {
                refsPorUsuario.computeIfAbsent(externo.userId(), clave -> new ArrayList<>())
                        .add(externo.orderRef());
            }
        }
        pedidos.guardarTodos(pedidosAGuardar);

        int clientesActualizados = 0;
        TreeSet<String> sinCliente = new TreeSet<>();
        for (Map.Entry<String, List<String>> entrada : refsPorUsuario.entrySet()) {
            if (vinculador.vincular(entrada.getKey(), entrada.getValue())) {
                clientesActualizados++;
            } else {
                sinCliente.add(entrada.getKey());
            }
        }
        return new ResultadoSincronizacion(pedidosAGuardar.size(), items.size(), sinDetalle,
                clientesActualizados, List.copyOf(sinCliente));
    }

    private Armado armar(PedidoExterno externo, Map<String, ItemExterno> itemsPorId) {
        List<PedidoItem> detalle = new ArrayList<>();
        int sinDetalle = 0;
        List<String> ids = externo.items() == null ? List.of() : externo.items();
        for (String itemId : ids) {
            ItemExterno item = itemsPorId.get(itemId);
            if (item == null) {
                sinDetalle++;
                LOG.warn("Pedido {}: el item {} no existe en /items", externo.orderRef(), itemId);
                detalle.add(PedidoItem.sinDetalle(itemId));
            } else {
                detalle.add(new PedidoItem(item.itemId(), item.skuId(), item.displayName(),
                        item.quantity(), item.deliveryStatus()));
            }
        }
        Pedido pedido = new Pedido(externo.orderRef(), externo.userId(), externo.canal(),
                externo.orderStatus(), Boolean.TRUE.equals(externo.marketPlace()),
                Boolean.TRUE.equals(externo.giftRegistry()), externo.storeName(), detalle);
        return new Armado(pedido, sinDetalle);
    }

    /**
     * Pedido ya cruzado con sus items y cantidad de items sin detalle.
     */
    private record Armado(Pedido pedido, int sinDetalle) {
    }
}
