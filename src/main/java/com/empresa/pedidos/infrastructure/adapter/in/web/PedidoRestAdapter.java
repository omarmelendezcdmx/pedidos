package com.empresa.pedidos.infrastructure.adapter.in.web;

import com.empresa.pedidos.application.port.in.PedidoUseCase;
import com.empresa.pedidos.domain.model.Pedido;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.PedidoRequest;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.PedidoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada HTTP para los casos de uso de pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "itemId, cantidad, canal de venta y estatus (fecha estimada de entrega)")
public class PedidoRestAdapter {

    private final PedidoUseCase pedidos;

    /**
     * Crea el adaptador.
     *
     * @param pedidos casos de uso de pedidos
     */
    public PedidoRestAdapter(PedidoUseCase pedidos) {
        this.pedidos = pedidos;
    }

    /**
     * Crea un pedido.
     *
     * @param request datos del pedido
     * @return pedido creado con código 201
     */
    @PostMapping
    @Operation(summary = "Crear pedido", description = "Agrega el orderRef al campo orders del cliente si existe")
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest request) {
        Pedido creado = pedidos.crear(WebMapper.aComando(request));
        return ResponseEntity.created(URI.create("/api/pedidos/" + creado.orderRef()))
                .body(WebMapper.aRespuesta(creado));
    }

    /**
     * Lista pedidos.
     *
     * @param userId cliente opcional para filtrar
     * @return pedidos
     */
    @GetMapping
    @Operation(summary = "Listar pedidos, opcionalmente por userId")
    public List<PedidoResponse> listar(@RequestParam(required = false) String userId) {
        return pedidos.listar(userId).stream().map(WebMapper::aRespuesta).toList();
    }

    /**
     * Consulta un pedido.
     *
     * @param orderRef número de pedido
     * @return el pedido
     */
    @GetMapping("/{orderRef}")
    @Operation(summary = "Consultar pedido")
    public PedidoResponse obtener(@PathVariable String orderRef) {
        return WebMapper.aRespuesta(pedidos.obtener(orderRef));
    }

    /**
     * Actualiza un pedido.
     *
     * @param orderRef número de pedido
     * @param request  nuevos datos
     * @return pedido actualizado
     */
    @PutMapping("/{orderRef}")
    @Operation(summary = "Actualizar pedido")
    public PedidoResponse actualizar(@PathVariable String orderRef, @Valid @RequestBody PedidoRequest request) {
        return WebMapper.aRespuesta(pedidos.actualizar(orderRef, WebMapper.aComando(request)));
    }

    /**
     * Elimina un pedido.
     *
     * @param orderRef número de pedido
     * @return respuesta vacía con código 204
     */
    @DeleteMapping("/{orderRef}")
    @Operation(summary = "Eliminar pedido")
    public ResponseEntity<Void> eliminar(@PathVariable String orderRef) {
        pedidos.eliminar(orderRef);
        return ResponseEntity.noContent().build();
    }
}
