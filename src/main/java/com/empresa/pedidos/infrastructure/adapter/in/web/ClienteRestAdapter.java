package com.empresa.pedidos.infrastructure.adapter.in.web;

import com.empresa.pedidos.application.port.in.ClienteUseCase;
import com.empresa.pedidos.domain.model.Cliente;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.ClienteRequest;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.ClienteResponse;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada HTTP para los casos de uso de clientes.
 *
 * Solo traduce HTTP a comandos y respuestas; no contiene lógica de negocio.
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Datos del cliente; el userId se vincula con el de /pedidos")
public class ClienteRestAdapter {

    private final ClienteUseCase clientes;

    /**
     * Crea el adaptador.
     *
     * @param clientes casos de uso de clientes
     */
    public ClienteRestAdapter(ClienteUseCase clientes) {
        this.clientes = clientes;
    }

    /**
     * Crea un cliente.
     *
     * @param request datos del cliente
     * @return cliente creado con código 201
     */
    @PostMapping
    @Operation(summary = "Crear cliente",
            description = "Si no se envía userId se genera uno; los pedidos existentes con ese userId se vinculan")
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        Cliente creado = clientes.crear(WebMapper.aComando(request));
        return ResponseEntity.created(URI.create("/api/clientes/" + creado.userId()))
                .body(WebMapper.aRespuesta(creado));
    }

    /**
     * Lista los clientes.
     *
     * @return clientes registrados
     */
    @GetMapping
    @Operation(summary = "Listar clientes")
    public List<ClienteResponse> listar() {
        return clientes.listar().stream().map(WebMapper::aRespuesta).toList();
    }

    /**
     * Consulta un cliente.
     *
     * @param userId identificador
     * @return el cliente, con el campo orders
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Consultar cliente (incluye orders con los orderRef)")
    public ClienteResponse obtener(@PathVariable String userId) {
        return WebMapper.aRespuesta(clientes.obtener(userId));
    }

    /**
     * Consulta el detalle de los pedidos de un cliente.
     *
     * @param userId identificador
     * @return pedidos del cliente
     */
    @GetMapping("/{userId}/pedidos")
    @Operation(summary = "Detalle de los pedidos del cliente")
    public List<PedidoResponse> pedidos(@PathVariable String userId) {
        return clientes.pedidosDe(userId).stream().map(WebMapper::aRespuesta).toList();
    }

    /**
     * Actualiza un cliente.
     *
     * @param userId  identificador
     * @param request nuevos datos
     * @return cliente actualizado
     */
    @PutMapping("/{userId}")
    @Operation(summary = "Actualizar cliente", description = "orders no se modifica: lo administra el sistema")
    public ClienteResponse actualizar(@PathVariable String userId, @Valid @RequestBody ClienteRequest request) {
        return WebMapper.aRespuesta(clientes.actualizar(userId, WebMapper.aComando(request)));
    }

    /**
     * Elimina un cliente y sus datos de entrega.
     *
     * @param userId identificador
     * @return respuesta vacía con código 204
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "Eliminar cliente y sus datos de entrega")
    public ResponseEntity<Void> eliminar(@PathVariable String userId) {
        clientes.eliminar(userId);
        return ResponseEntity.noContent().build();
    }
}
