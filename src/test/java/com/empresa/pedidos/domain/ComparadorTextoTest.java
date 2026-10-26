package com.empresa.pedidos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.empresa.pedidos.domain.busqueda.ComparadorTexto;
import org.junit.jupiter.api.Test;

class ComparadorTextoTest {

    @Test
    void normalizaAcentosMayusculasComasYApostrofos() {
        assertThat(ComparadorTexto.normalizar("Pantalón Levi´s,")).isEqualTo("pantalon levis");
        assertThat(ComparadorTexto.normalizar("L  SANTA FE")).isEqualTo("l santa fe");
        assertThat(ComparadorTexto.normalizar("Liverpool Galerías, Toluca")).isEqualTo("liverpool galerias toluca");
        assertThat(ComparadorTexto.normalizar(null)).isEmpty();
    }

    @Test
    void elPrefijoEsCoincidenciaExacta() {
        assertThat(ComparadorTexto.coincidencia("pant", "Pantalón Levi´s", true)).isZero();
        assertThat(ComparadorTexto.coincidencia("PANTALON, LEVIS", "Pantalón Levi´s", true)).isZero();
        assertThat(ComparadorTexto.coincidencia("galerias tol", "Liverpool Galerías Toluca", true)).isZero();
    }

    @Test
    void toleraErroresMinimosDeOrtografia() {
        assertThat(ComparadorTexto.coincidencia("pantaln", "Pantalón Levi´s", true)).isPositive();
        assertThat(ComparadorTexto.coincidencia("laptp lenovo", "Laptop Lenovo thinkpad", true)).isPositive();
        assertThat(ComparadorTexto.coincidencia("liverpol", "Liverpool Angelópolis", true)).isPositive();
    }

    @Test
    void textoSinEspaciosEncuentraPalabrasSeparadas() {
        assertThat(ComparadorTexto.coincidencia("playstation", "Play Station 5", true)).isPositive();
    }

    @Test
    void noCoincideConTextoDistinto() {
        assertThat(ComparadorTexto.coincidencia("zapatos", "Pantalón Levi´s", true))
                .isEqualTo(ComparadorTexto.NO_COINCIDE);
        assertThat(ComparadorTexto.coincidencia("monitor", "Mouse logitech", true))
                .isEqualTo(ComparadorTexto.NO_COINCIDE);
        assertThat(ComparadorTexto.coincidencia("algo", null, true)).isEqualTo(ComparadorTexto.NO_COINCIDE);
    }

    @Test
    void camposNumericosNoToleranErrores() {
        assertThat(ComparadorTexto.coincidencia("30100916", "3010091676", false)).isZero();
        assertThat(ComparadorTexto.coincidencia("30100917", "3010091676", false))
                .isEqualTo(ComparadorTexto.NO_COINCIDE);
        assertThat(ComparadorTexto.coincidencia("2025-12", "2025-12-06", false)).isZero();
    }

    @Test
    void consultaVaciaCoincideConTodo() {
        assertThat(ComparadorTexto.coincidencia("  ,, ", "lo que sea", true)).isZero();
    }
}
