package com.empresa.pedidos.infrastructure.adapter.in.web;

import com.empresa.pedidos.application.port.in.BusquedaUseCase;
import com.empresa.pedidos.domain.busqueda.CampoSugerible;
import com.empresa.pedidos.domain.busqueda.FiltroPedidos;
import com.empresa.pedidos.domain.exception.RecursoNoEncontradoException;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.ApiResponse;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.BusquedaPedidoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador de entrada HTTP para la búsqueda type-ahead de pedidos.
 */
@RestController
@RequestMapping("/api/busqueda")
@Tag(name = "Búsqueda", description = "Type-ahead flexible: ignora mayúsculas, acentos y comas")
public class BusquedaRestAdapter {

    private static final int LIMITE_MAXIMO_BUSQUEDA = 100;
    private static final int LIMITE_MAXIMO_SUGERENCIAS = 50;

    private final BusquedaUseCase busqueda;

    /**
     * Crea el adaptador.
     *
     * @param busqueda casos de uso de búsqueda
     */
    public BusquedaRestAdapter(BusquedaUseCase busqueda) {
        this.busqueda = busqueda;
    }

    /**
     * Busca pedidos; todos los filtros informados se combinan con AND.
     *
     * @param orderRef    prefijo del número de pedido
     * @param orderStatus prefijo de la fecha estimada
     * @param storeName   texto de la tienda
     * @param displayName texto del producto
     * @param q           texto libre sobre todos los campos
     * @param limit       máximo de resultados
     * @return pedidos ordenados por relevancia
     */
    @GetMapping("/pedidos")
    @Operation(summary = "Buscar pedidos",
            description = "Con displayName solo se devuelven los items que coinciden")
    public List<BusquedaPedidoResponse> buscar(
            @RequestParam(required = false) @Parameter(example = "30100916") String orderRef,
            @RequestParam(required = false) @Parameter(example = "2025-12") String orderStatus,
            @RequestParam(required = false) @Parameter(example = "galerias toluca") String storeName,
            @RequestParam(required = false) @Parameter(example = "pantalon levis") String displayName,
            @RequestParam(required = false) @Parameter(description = "Texto libre") String q,
            @RequestParam(defaultValue = "20") int limit) {
        FiltroPedidos filtro = new FiltroPedidos(orderRef, orderStatus, storeName, displayName, q);
        return busqueda.buscar(filtro, acotar(limit, LIMITE_MAXIMO_BUSQUEDA)).stream()
                .map(WebMapper::aRespuesta)
                .toList();
    }

    /**
     * Sugiere valores de autocompletado para un campo.
     *
     * @param campo orderRef, orderStatus, storeName o displayName
     * @param q     texto escrito
     * @param limit máximo de sugerencias
     * @return sugerencias ordenadas
     */
    @GetMapping("/sugerencias")
    @Operation(summary = "Autocompletado de un campo",
            description = "campo = orderRef | orderStatus | storeName | displayName")
    public ResponseEntity<ApiResponse<List<String>>> sugerencias(
            @RequestParam String campo,
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "10") int limit) {
        //Retornar 200 ok y 404 en caso de error
        List<String> lista =  busqueda.sugerir(CampoSugerible.desde(campo), q,
                acotar(limit, LIMITE_MAXIMO_SUGERENCIAS));

        if (lista == null || lista.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No se encontraron resultados"
            );
        }
        return ResponseEntity.ok(ApiResponse.success(lista));
    }

    private static int acotar(int valor, int maximo) {
        return Math.max(1, Math.min(valor, maximo));
    }
}
