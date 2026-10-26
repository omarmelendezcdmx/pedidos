package com.empresa.pedidos.infrastructure.adapter.in.web;

import com.empresa.pedidos.application.port.in.EntregaUseCase;
import com.empresa.pedidos.domain.model.Entrega;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.EntregaRequest;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.EntregaResponse;
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
 * Adaptador de entrada HTTP para los casos de uso de datos de entrega.
 */
@RestController
@RequestMapping("/api/entregas")
@Tag(name = "Entregas", description = "Datos de entrega (dirección de envío)")
public class EntregaRestAdapter {

    private final EntregaUseCase entregas;

    /**
     * Crea el adaptador.
     *
     * @param entregas casos de uso de entregas
     */
    public EntregaRestAdapter(EntregaUseCase entregas) {
        this.entregas = entregas;
    }

    /**
     * Crea una entrega.
     *
     * @param request datos de la entrega
     * @return entrega creada con código 201
     */
    @PostMapping
    @Operation(summary = "Crear dato de entrega (el cliente debe existir)")
    public ResponseEntity<EntregaResponse> crear(@Valid @RequestBody EntregaRequest request) {
        Entrega creada = entregas.crear(WebMapper.aComando(request));
        return ResponseEntity.created(URI.create("/api/entregas/" + creada.id()))
                .body(WebMapper.aRespuesta(creada));
    }

    /**
     * Lista entregas.
     *
     * @param userId cliente opcional para filtrar
     * @return entregas
     */
    @GetMapping
    @Operation(summary = "Listar entregas, opcionalmente por userId")
    public List<EntregaResponse> listar(@RequestParam(required = false) String userId) {
        return entregas.listar(userId).stream().map(WebMapper::aRespuesta).toList();
    }

    /**
     * Consulta una entrega.
     *
     * @param id identificador
     * @return la entrega
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar entrega")
    public EntregaResponse obtener(@PathVariable String id) {
        return WebMapper.aRespuesta(entregas.obtener(id));
    }

    /**
     * Actualiza una entrega.
     *
     * @param id      identificador
     * @param request nuevos datos
     * @return entrega actualizada
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar entrega")
    public EntregaResponse actualizar(@PathVariable String id, @Valid @RequestBody EntregaRequest request) {
        return WebMapper.aRespuesta(entregas.actualizar(id, WebMapper.aComando(request)));
    }

    /**
     * Elimina una entrega.
     *
     * @param id identificador
     * @return respuesta vacía con código 204
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar entrega")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        entregas.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
