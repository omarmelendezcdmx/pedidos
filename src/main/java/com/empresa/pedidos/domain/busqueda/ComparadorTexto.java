package com.empresa.pedidos.domain.busqueda;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Comparación flexible de texto para búsquedas type-ahead.
 *
 * Ignora mayúsculas, acentos, comas y apóstrofos, coincide por prefijo de palabra y
 * puede tolerar errores mínimos de ortografía mediante distancia de Levenshtein.
 */
public final class ComparadorTexto {

    /** Valor devuelto por {@link #coincidencia} cuando el texto no coincide. */
    public static final int NO_COINCIDE = -1;

    private static final int INFINITA = Integer.MAX_VALUE;
    private static final int LARGO_PARA_UN_ERROR = 4;
    private static final int LARGO_PARA_DOS_ERRORES = 8;
    private static final int LARGO_MINIMO_TEXTO_PEGADO = 4;

    private ComparadorTexto() {
        // Clase de utilidad: no instanciable.
    }

    /**
     * Normaliza un texto: minúsculas, sin acentos, sin puntuación ni apóstrofos.
     *
     * @param texto texto original (puede ser null)
     * @return texto normalizado, con palabras separadas por un espacio
     */
    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String limpio = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        limpio = limpio.toLowerCase(Locale.ROOT);
        limpio = limpio.replaceAll("[´`'’‘ʼ]", "");
        limpio = limpio.replaceAll("[^\\p{L}\\p{N}]+", " ");
        return limpio.trim();
    }

    /**
     * Evalúa si todas las palabras de la consulta coinciden con el texto.
     *
     * @param consulta        lo que escribió el usuario
     * @param texto           texto contra el que se compara
     * @param tolerarErrores  false para códigos y fechas, que solo admiten prefijo
     * @return {@link #NO_COINCIDE} si no coincide; si coincide, un puntaje donde 0 es
     *         coincidencia exacta o por prefijo y valores mayores indican más tolerancia
     */
    public static int coincidencia(String consulta, String texto, boolean tolerarErrores) {
        List<String> palabrasConsulta = palabras(normalizar(consulta));
        if (palabrasConsulta.isEmpty()) {
            return 0;
        }
        String textoNormalizado = normalizar(texto);
        List<String> palabrasTexto = palabras(textoNormalizado);
        String textoPegado = textoNormalizado.replace(" ", "");

        int total = 0;
        for (String palabra : palabrasConsulta) {
            int mejor = mejorDistancia(palabra, palabrasTexto, tolerarErrores);
            if (mejor == INFINITA && tolerarErrores
                    && palabra.length() >= LARGO_MINIMO_TEXTO_PEGADO && textoPegado.contains(palabra)) {
                mejor = 1;
            }
            if (mejor == INFINITA) {
                return NO_COINCIDE;
            }
            total += mejor;
        }
        return total;
    }

    private static int mejorDistancia(String palabra, List<String> candidatas, boolean tolerarErrores) {
        int mejor = INFINITA;
        for (String candidata : candidatas) {
            mejor = Math.min(mejor, distancia(palabra, candidata, tolerarErrores));
            if (mejor == 0) {
                break;
            }
        }
        return mejor;
    }

    private static List<String> palabras(String normalizado) {
        return normalizado.isEmpty() ? List.of() : Arrays.asList(normalizado.split(" "));
    }

    private static int distancia(String consulta, String candidata, boolean tolerarErrores) {
        if (candidata.startsWith(consulta)) {
            return 0;
        }
        if (!tolerarErrores || consulta.chars().anyMatch(Character::isDigit)) {
            return INFINITA;
        }
        int maximo = erroresPermitidos(consulta.length());
        if (maximo == 0) {
            return INFINITA;
        }
        String inicio = candidata.length() > consulta.length()
                ? candidata.substring(0, consulta.length())
                : candidata;
        int distancia = Math.min(obtenerDistanciaEntreCadenas(consulta, inicio),
                obtenerDistanciaEntreCadenas(consulta, candidata));
        return distancia <= maximo ? distancia : INFINITA;
    }

    private static int erroresPermitidos(int largo) {
        if (largo >= LARGO_PARA_DOS_ERRORES) {
            return 2;
        }
        return largo >= LARGO_PARA_UN_ERROR ? 1 : 0;
    }

    /**
     * Distancia de edición entre dos cadenas. Levenshtein
     *
     * @param a primera cadena
     * @param b segunda cadena
     * @return número mínimo de inserciones, borrados o sustituciones
     */
    static int obtenerDistanciaEntreCadenas(String a, String b) {
        int[] anterior = new int[b.length() + 1];
        int[] actual = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) {
            anterior[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            actual[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int costo = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                actual[j] = Math.min(Math.min(actual[j - 1] + 1, anterior[j] + 1), anterior[j - 1] + costo);
            }
            int[] intercambio = anterior;
            anterior = actual;
            actual = intercambio;
        }
        return anterior[b.length()];
    }
}
