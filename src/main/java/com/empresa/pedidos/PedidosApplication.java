package com.empresa.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación de gestión de pedidos.
 */
@SpringBootApplication
public class PedidosApplication {

    /**
     * Arranca la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(PedidosApplication.class, args);
    }
}
