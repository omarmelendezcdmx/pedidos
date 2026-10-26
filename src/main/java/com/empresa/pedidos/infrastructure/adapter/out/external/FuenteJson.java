package com.empresa.pedidos.infrastructure.adapter.out.external;

import com.empresa.pedidos.application.exception.ServicioExternoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Lee una lista JSON desde una URL http(s) o desde el classpath (prefijo classpath:),
 * lo que permite desarrollar sin los servicios reales.
 */
@Component
public class FuenteJson {

    private static final String PREFIJO_CLASSPATH = "classpath:";

    private final RestClient restClient;
    private final ObjectMapper mapper;

    /**
     * Crea la fuente.
     *
     * @param restClient cliente HTTP
     * @param mapper     serializador JSON
     */
    public FuenteJson(RestClient restClient, ObjectMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    /**
     * Lee y deserializa una lista JSON.
     *
     * @param url  URL http(s) o ruta classpath:
     * @param tipo tipo de los elementos
     * @param <T>  tipo de los elementos
     * @return elementos leídos
     * @throws ServicioExternoException si el origen no responde o el JSON es inválido
     */
    public <T> List<T> leerLista(String url, Class<T> tipo) {
        var tipoLista = mapper.getTypeFactory().constructCollectionType(List.class, tipo);
        try {
            if (url.startsWith(PREFIJO_CLASSPATH)) {
                String ruta = url.substring(PREFIJO_CLASSPATH.length());
                try (InputStream entrada = new ClassPathResource(ruta).getInputStream()) {
                    return mapper.readValue(entrada, tipoLista);
                }
            }
            String json = restClient.get().uri(url).retrieve().body(String.class);
            return mapper.readValue(json, tipoLista);
        } catch (IOException | RestClientException e) {
            throw new ServicioExternoException("No se pudo consultar " + url + ": " + e.getMessage(), e);
        }
    }
}
