package com.tallerwebi.dominio.apiPregunta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.util.List;

class ApiPreguntaTest {

    private ApiPregunta pregunta;

    @BeforeEach
    void setUp() {
        pregunta = new ApiPregunta();
        pregunta.setQuestion("What is the capital of Spain?");
        pregunta.setCorrectAnswer("Madrid");
        pregunta.setIncorrectAnswers(List.of("Barcelona", "Valencia", "Sevilla"));
        pregunta.setDifficulty("easy");
        pregunta.setType("multiple");
    }

    @Test
    void testDecodificacionUrlEncoding() {
        // ARRANGE
        pregunta.setQuestion("¿Qué es %22Hola%22%3F");

        // ACT
        String resultado = pregunta.getPreguntaDecodificada();

        // ASSERT
        assertThat(resultado).contains("¿Qué es");
        assertThat(resultado).doesNotContain("%22");
        assertThat(resultado).doesNotContain("%3F");
    }

    @Test
    void testDecodificacionHtmlEntities() {
        // ARRANGE
        pregunta.setQuestion("¿Qué es &quot;Hola&quot;?");

        // ACT
        String resultado = pregunta.getPreguntaDecodificada();

        // ASSERT
        assertThat(resultado).contains("\"");
        assertThat(resultado).doesNotContain("&quot;");
    }

    @Test
    void testGetTodasLasOpciones() {
        // ACT
        List<String> opciones = pregunta.getTodasLasOpciones();

        // ASSERT
        assertThat(opciones).hasSize(4);
        assertThat(opciones).contains("Madrid", "Barcelona", "Valencia", "Sevilla");
    }

    @Test
    void testOcionesAleatorias() {
        // ACT - Obtener opciones 2 veces
        List<String> opciones1 = pregunta.getTodasLasOpciones();
        List<String> opciones2 = pregunta.getTodasLasOpciones();

        // ASSERT - Pueden estar en diferente orden (no garantizado pero muy probable)
        assertThat(opciones1).containsExactlyInAnyOrder("Madrid", "Barcelona", "Valencia", "Sevilla");
        assertThat(opciones2).containsExactlyInAnyOrder("Madrid", "Barcelona", "Valencia", "Sevilla");
    }

    @Test
    void testDificultadTraducida() {
        // ARRANGE
        pregunta.setDifficulty("easy");

        // ACT
        String dificultad = pregunta.getDifficulty();

        // ASSERT
        assertThat(dificultad).isEqualTo("Fácil");
    }

    @Test
    void testDificultadTraduccidaMedium() {
        // ARRANGE
        pregunta.setDifficulty("medium");

        // ACT
        String dificultad = pregunta.getDifficulty();

        // ASSERT
        assertThat(dificultad).isEqualTo("Medio");
    }

    @Test
    void testDificultadTraduccidaHard() {
        // ARRANGE
        pregunta.setDifficulty("hard");

        // ACT
        String dificultad = pregunta.getDifficulty();

        // ASSERT
        assertThat(dificultad).isEqualTo("Difícil");
    }

    @Test
    void testRespuestaCorrectaDecodificada() {
        // ARRANGE
        pregunta.setCorrectAnswer("Madrid%20City");

        // ACT
        String respuesta = pregunta.getRespuestaCorrectaDecodificada();

        // ASSERT
        assertThat(respuesta).contains("Madrid");
        assertThat(respuesta).doesNotContain("%20");
    }
}

