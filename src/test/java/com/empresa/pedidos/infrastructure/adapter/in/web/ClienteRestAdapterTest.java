package com.empresa.pedidos.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.empresa.pedidos.application.command.ClienteCommand;
import com.empresa.pedidos.application.port.in.ClienteUseCase;
import com.empresa.pedidos.domain.exception.ConflictoException;
import com.empresa.pedidos.domain.exception.RecursoNoEncontradoException;
import com.empresa.pedidos.domain.model.Cliente;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ClienteRestAdapterTest {

    private final ClienteUseCase useCase = mock(ClienteUseCase.class);
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new ClienteRestAdapter(useCase))
            .setControllerAdvice(new ApiExceptionHandler())
            .build();

    private static final String CUERPO_VALIDO = """
            {"userId":"u1","nombre":"Ana","apellidoPaterno":"López","apellidoMaterno":"Ruiz",
             "email":"ana@correo.com"}""";

    @Test
    void consultarDevuelveElClienteConSusOrders() throws Exception {
        when(useCase.obtener("u1")).thenReturn(
                new Cliente("u1", "Ana", "López", "Ruiz", "ana@correo.com", List.of("1001", "1002")));

        mvc.perform(get("/api/clientes/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("u1"))
                .andExpect(jsonPath("$.orders[0]").value("1001"))
                .andExpect(jsonPath("$.orders[1]").value("1002"));
    }

    @Test
    void consultarInexistenteDevuelve404() throws Exception {
        when(useCase.obtener("x")).thenThrow(new RecursoNoEncontradoException("Cliente no encontrado: x"));

        mvc.perform(get("/api/clientes/x")).andExpect(status().isNotFound());
    }

    @Test
    void crearDevuelve201ConLaUbicacionDelRecurso() throws Exception {
        when(useCase.crear(any(ClienteCommand.class))).thenReturn(
                new Cliente("u1", "Ana", "López", "Ruiz", "ana@correo.com", List.of()));

        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(CUERPO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/clientes/u1"));
    }

    @Test
    void crearConCorreoInvalidoDevuelve400SinLlamarAlCasoDeUso() throws Exception {
        String invalido = "{\"nombre\":\"Ana\",\"apellidoPaterno\":\"López\",\"email\":\"no-es-correo\"}";

        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(invalido))
                .andExpect(status().isBadRequest());
        org.mockito.Mockito.verifyNoInteractions(useCase);
    }

    @Test
    void crearDuplicadoDevuelve409() throws Exception {
        when(useCase.crear(any(ClienteCommand.class))).thenThrow(new ConflictoException("duplicado"));

        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(CUERPO_VALIDO))
                .andExpect(status().isConflict());
    }

    @Test
    void eliminarDevuelve204() throws Exception {
        mvc.perform(delete("/api/clientes/u1")).andExpect(status().isNoContent());

        verify(useCase).eliminar("u1");
    }
}
