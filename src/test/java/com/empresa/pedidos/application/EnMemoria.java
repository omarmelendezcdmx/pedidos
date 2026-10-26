package com.empresa.pedidos.application;

import com.empresa.pedidos.application.port.out.ClienteRepositorio;
import com.empresa.pedidos.application.port.out.EntregaRepositorio;
import com.empresa.pedidos.application.port.out.PedidoRepositorio;
import com.empresa.pedidos.domain.model.Cliente;
import com.empresa.pedidos.domain.model.Entrega;
import com.empresa.pedidos.domain.model.Pedido;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Implementaciones en memoria de los puertos de salida, para probar sin Spring ni Mongo. */
public final class EnMemoria {

    private EnMemoria() {
    }

    public static final class Clientes implements ClienteRepositorio {
        private final Map<String, Cliente> datos = new LinkedHashMap<>();

        @Override
        public Cliente guardar(Cliente cliente) {
            datos.put(cliente.userId(), cliente);
            return cliente;
        }

        @Override
        public Optional<Cliente> buscarPorId(String userId) {
            return Optional.ofNullable(datos.get(userId));
        }

        @Override
        public Optional<Cliente> buscarPorEmail(String email) {
            return datos.values().stream().filter(c -> c.email().equalsIgnoreCase(email)).findFirst();
        }

        @Override
        public boolean existePorId(String userId) {
            return datos.containsKey(userId);
        }

        @Override
        public List<Cliente> buscarTodos() {
            return new ArrayList<>(datos.values());
        }

        @Override
        public void eliminar(String userId) {
            datos.remove(userId);
        }
    }

    public static final class Entregas implements EntregaRepositorio {
        private final Map<String, Entrega> datos = new LinkedHashMap<>();

        @Override
        public Entrega guardar(Entrega entrega) {
            String id = entrega.id() == null ? UUID.randomUUID().toString() : entrega.id();
            Entrega guardada = new Entrega(id, entrega.userId(), entrega.orderRef(), entrega.direccionEnvio());
            datos.put(id, guardada);
            return guardada;
        }

        @Override
        public Optional<Entrega> buscarPorId(String id) {
            return Optional.ofNullable(datos.get(id));
        }

        @Override
        public List<Entrega> buscarTodas() {
            return new ArrayList<>(datos.values());
        }

        @Override
        public List<Entrega> buscarPorUsuario(String userId) {
            return datos.values().stream().filter(e -> e.userId().equals(userId)).toList();
        }

        @Override
        public void eliminar(String id) {
            datos.remove(id);
        }

        @Override
        public void eliminarPorUsuario(String userId) {
            datos.values().removeIf(e -> e.userId().equals(userId));
        }
    }

    public static final class Pedidos implements PedidoRepositorio {
        private final Map<String, Pedido> datos = new LinkedHashMap<>();

        @Override
        public Pedido guardar(Pedido pedido) {
            datos.put(pedido.orderRef(), pedido);
            return pedido;
        }

        @Override
        public void guardarTodos(Collection<Pedido> pedidos) {
            pedidos.forEach(this::guardar);
        }

        @Override
        public Optional<Pedido> buscarPorId(String orderRef) {
            return Optional.ofNullable(datos.get(orderRef));
        }

        @Override
        public boolean existePorId(String orderRef) {
            return datos.containsKey(orderRef);
        }

        @Override
        public List<Pedido> buscarTodos() {
            return new ArrayList<>(datos.values());
        }

        @Override
        public List<Pedido> buscarPorUsuario(String userId) {
            return datos.values().stream().filter(p -> userId.equals(p.userId())).toList();
        }

        @Override
        public void eliminar(String orderRef) {
            datos.remove(orderRef);
        }
    }
}
