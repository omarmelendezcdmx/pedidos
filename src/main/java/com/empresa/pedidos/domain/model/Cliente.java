package com.empresa.pedidos.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Cliente del sistema.
 *
 * Se identifica con el mismo userId que expone el servicio de pedidos y conserva en
 * orders los orderRef de sus pedidos. Es inmutable: cada cambio devuelve una copia.
 *
 * @param userId          identificador del usuario
 * @param nombre          nombre de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param email           correo electrónico
 * @param orders          orderRef de los pedidos asociados
 */
public record Cliente(
        String userId,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String email,
        List<String> orders) {

    /**
     * Garantiza que la lista de pedidos sea inmutable y nunca nula.
     */
    public Cliente {
        orders = orders == null ? List.of() : List.copyOf(orders);
    }

    /**
     * Copia del cliente con nuevos datos personales, conservando identificador y pedidos.
     *
     * @param nuevoNombre   nombre
     * @param nuevoPaterno  apellido paterno
     * @param nuevoMaterno  apellido materno
     * @param nuevoEmail    correo electrónico
     * @return cliente actualizado
     */
    public Cliente conDatos(String nuevoNombre, String nuevoPaterno, String nuevoMaterno, String nuevoEmail) {
        return new Cliente(userId, nuevoNombre, nuevoPaterno, nuevoMaterno, nuevoEmail, orders);
    }

    /**
     * Copia del cliente con los pedidos indicados agregados, sin duplicados.
     *
     * @param orderRefs referencias de pedido a asociar
     * @return cliente con los pedidos asociados
     */
    public Cliente conPedidos(Collection<String> orderRefs) {
        Set<String> unicos = new LinkedHashSet<>(orders);
        unicos.addAll(orderRefs);
        return new Cliente(userId, nombre, apellidoPaterno, apellidoMaterno, email, new ArrayList<>(unicos));
    }

    /**
     * Copia del cliente sin el pedido indicado.
     *
     * @param orderRef referencia del pedido a desasociar
     * @return cliente sin ese pedido
     */
    public Cliente sinPedido(String orderRef) {
        List<String> restantes = new ArrayList<>(orders);
        restantes.remove(orderRef);
        return new Cliente(userId, nombre, apellidoPaterno, apellidoMaterno, email, restantes);
    }
}
