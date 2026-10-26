package com.empresa.pedidos.infrastructure.adapter.in.web;

import com.empresa.pedidos.application.port.in.SincronizacionUseCase;
import com.empresa.pedidos.infrastructure.adapter.in.web.dto.SincronizacionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada HTTP para sincronizar con los servicios externos.
 */
@RestController
@RequestMapping("/api/sync")
@Tag(name = "Sincronización", description = "Consulta /pedidos e /items y persiste en MongoDB")
public class SincronizacionRestAdapter {

    private final SincronizacionUseCase sincronizacion;

    /**
     * Crea el adaptador.
     *
     * @param sincronizacion caso de uso de sincronización
     */
    public SincronizacionRestAdapter(SincronizacionUseCase sincronizacion) {
        this.sincronizacion = sincronizacion;
    }

    /**
     * Ejecuta la sincronización.
     *
     * @return resumen de lo procesado
     */
    @PostMapping
    @Operation(summary = "Sincronizar con los servicios externos",
            description = "Es idempotente: hace upsert por orderRef")
    public SincronizacionResponse sincronizar() {
        return WebMapper.aRespuesta(sincronizacion.sincronizar());
    }
}
