package com.empresa.pedidos.infrastructure.adapter.in.web;

import com.empresa.pedidos.application.command.ClienteCommand;
import com.empresa.pedidos.application.command.EntregaCommand;
import com.empresa.pedidos.application.command.PedidoCommand;
import com.empresa.pedidos.application.command.PedidoCommand.ItemCommand;
import com.empresa.pedidos.application.result.ResultadoSincronizacion;
import com.empresa.pedidos.domain.busqueda.ResultadoBusqueda;
import com.empresa.pedidos.domain.model.Cliente;
import com.empresa.pedidos.domain.model.Entrega;
import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.domain.model.PedidoItem;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.BusquedaPedidoResponse;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.ClienteRequest;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.ClienteResponse;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.EntregaRequest;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.EntregaResponse;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.PedidoRequest;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.PedidoResponse;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.PedidoResponse.ItemResponse;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.SincronizacionResponse;
import java.util.List;

/**
 * Traduce entre los DTO del adaptador HTTP y los objetos de la aplicación y el dominio.
 */
public final class WebMapper {

    private WebMapper() {
        // Clase de utilidad: no instanciable.
    }

    /**
     * Convierte una petición de cliente en comando.
     *
     * @param request cuerpo HTTP
     * @return comando de aplicación
     */
    public static ClienteCommand aComando(ClienteRequest request) {
        return new ClienteCommand(request.userId(), request.nombre(), request.apellidoPaterno(),
                request.apellidoMaterno(), request.email());
    }

    /**
     * Convierte una petición de entrega en comando.
     *
     * @param request cuerpo HTTP
     * @return comando de aplicación
     */
    public static EntregaCommand aComando(EntregaRequest request) {
        return new EntregaCommand(request.userId(), request.orderRef(), request.direccionEnvio());
    }

    /**
     * Convierte una petición de pedido en comando.
     *
     * @param request cuerpo HTTP
     * @return comando de aplicación
     */
    public static PedidoCommand aComando(PedidoRequest request) {
        List<ItemCommand> items = request.items().stream()
                .map(item -> new ItemCommand(item.itemId(), item.cantidad(), item.displayName()))
                .toList();
        return new PedidoCommand(request.orderRef(), request.userId(), request.canal(),
                request.orderStatus(), request.storeName(), Boolean.TRUE.equals(request.marketPlace()),
                Boolean.TRUE.equals(request.giftRegistry()), items);
    }

    /**
     * Convierte un cliente en su representación HTTP.
     *
     * @param cliente cliente de dominio
     * @return respuesta HTTP
     */
    public static ClienteResponse aRespuesta(Cliente cliente) {
        return new ClienteResponse(cliente.userId(), cliente.nombre(), cliente.apellidoPaterno(),
                cliente.apellidoMaterno(), cliente.email(), cliente.orders());
    }

    /**
     * Convierte una entrega en su representación HTTP.
     *
     * @param entrega entrega de dominio
     * @return respuesta HTTP
     */
    public static EntregaResponse aRespuesta(Entrega entrega) {
        return new EntregaResponse(entrega.id(), entrega.userId(), entrega.orderRef(),
                entrega.direccionEnvio());
    }

    /**
     * Convierte un pedido en su representación HTTP.
     *
     * @param pedido pedido de dominio
     * @return respuesta HTTP
     */
    public static PedidoResponse aRespuesta(Pedido pedido) {
        return new PedidoResponse(pedido.orderRef(), pedido.userId(), pedido.canal(), pedido.orderStatus(),
                pedido.marketPlace(), pedido.giftRegistry(), pedido.storeName(), aItems(pedido.items()));
    }

    /**
     * Convierte un resultado de búsqueda en su representación HTTP.
     *
     * @param resultado resultado de dominio
     * @return respuesta HTTP
     */
    public static BusquedaPedidoResponse aRespuesta(ResultadoBusqueda resultado) {
        Pedido pedido = resultado.pedido();
        return new BusquedaPedidoResponse(pedido.orderRef(), pedido.userId(), pedido.canal(),
                pedido.orderStatus(), pedido.storeName(), pedido.marketPlace(), pedido.giftRegistry(),
                aItems(resultado.items()), resultado.relevancia());
    }

    /**
     * Convierte el resumen de sincronización en su representación HTTP.
     *
     * @param resultado resumen de aplicación
     * @return respuesta HTTP
     */
    public static SincronizacionResponse aRespuesta(ResultadoSincronizacion resultado) {
        return new SincronizacionResponse(resultado.pedidosSincronizados(), resultado.itemsRecibidos(),
                resultado.itemsSinDetalle(), resultado.clientesActualizados(), resultado.userIdsSinCliente());
    }

    private static List<ItemResponse> aItems(List<PedidoItem> items) {
        return items.stream()
                .map(item -> new ItemResponse(item.itemId(), item.skuId(), item.displayName(),
                        item.cantidad(), item.deliveryStatus()))
                .toList();
    }
}
