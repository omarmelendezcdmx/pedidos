package com.empresa.pedidos.application;

import com.empresa.pedidos.application.command.ClienteCommand;
import com.empresa.pedidos.application.command.PedidoCommand;
import com.empresa.pedidos.application.command.PedidoCommand.ItemCommand;
import java.util.List;

/** Fábrica de comandos de ejemplo para las pruebas. */
public final class DatosDePrueba {

    public static final String USER_ANA = "75c97531-abf5-4524-8107-90aa48d08efc";
    public static final String USER_LUIS = "aa2ae8bf-6b32-45dc-bb69-14e899bd8fed";

    private DatosDePrueba() {
    }

    public static ClienteCommand clienteAna() {
        return new ClienteCommand(USER_ANA, "Ana", "López", "Ruiz", "ana@correo.com");
    }

    public static ClienteCommand clienteLuis() {
        return new ClienteCommand(USER_LUIS, "Luis", "Pérez", null, "luis@correo.com");
    }

    public static PedidoCommand pedido(String orderRef, String userId) {
        return new PedidoCommand(orderRef, userId, "online", "2025-12-06", "L SANTA FE", false, false,
                List.of(new ItemCommand(orderRef + "-111", 2, "Pantalón Levi´s")));
    }
}
