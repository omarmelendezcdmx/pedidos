package com.empresa.pedidos.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración técnica de HTTP: cliente saliente, CORS y documentación OpenAPI.
 */
@Configuration
public class WebConfig {

    private static final int TIMEOUT_CONEXION_SEGUNDOS = 5;
    private static final int TIMEOUT_LECTURA_SEGUNDOS = 15;

    @Bean
    RestClient restClient() {
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(Duration.ofSeconds(TIMEOUT_CONEXION_SEGUNDOS));
        fabrica.setReadTimeout(Duration.ofSeconds(TIMEOUT_LECTURA_SEGUNDOS));
        return RestClient.builder().requestFactory(fabrica).build();
    }

    @Bean
    WebMvcConfigurer corsConfigurer(@Value("${app.cors.origins}") String[] origenes) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registro) {
                registro.addMapping("/api/**")
                        .allowedOrigins(origenes)
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

    @Bean
    OpenAPI openApi() {
        return new OpenAPI().info(new Info()
                .title("API de gestión de pedidos")
                .version("2.0.0")
                .description("Clientes, entregas, pedidos y búsqueda type-ahead. "
                        + "Consolida los servicios externos /pedidos e /items. "
                        + "Arquitectura hexagonal (puertos y adaptadores)."));
    }
}
