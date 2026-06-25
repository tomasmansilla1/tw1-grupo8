package com.tallerwebi.presentacion.juegoControllerTest;

import com.tallerwebi.dominio.apiPregunta.ApiPregunta;
import com.tallerwebi.dominio.servicioPregunta.PreguntaService;
import com.tallerwebi.presentacion.juegoController.JuegoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JuegoControllerTest {

    private JuegoController juegoController;
    private PreguntaService preguntaService;
    private Model model;

    @BeforeEach
    public void init() {
        juegoController = new JuegoController();

        preguntaService = Mockito.mock(PreguntaService.class);
        model = Mockito.mock(Model.class);

        ReflectionTestUtils.setField(
                juegoController,
                "preguntaService",
                preguntaService
        );
    }

    @Test
    public void cuandoSeAccedeAInicioRetornaIndex() {

        String vista = juegoController.inicio();

        assertEquals("index", vista);
    }

    @Test
    public void cuandoSeIniciaJuegoSinDificultadRetornaVistaJuego() {

        ApiPregunta pregunta = mock(ApiPregunta.class);

        List<ApiPregunta> preguntas = Arrays.asList(pregunta);

        when(preguntaService.obtenerPreguntas(1))
                .thenReturn(preguntas);

        String vista = juegoController.iniciarJuego(1, "", model);

        assertEquals("juego", vista);

        verify(model).addAttribute("totalPreguntas", 1);
        verify(model).addAttribute("preguntaNumero", 1);
        verify(model).addAttribute("pregunta", pregunta);
    }

    @Test
    public void cuandoSeIniciaJuegoConDificultadRetornaVistaJuego() {

        ApiPregunta pregunta = mock(ApiPregunta.class);

        List<ApiPregunta> preguntas = Arrays.asList(pregunta);

        when(preguntaService.obtenerPreguntasPorDificultad(1, "easy"))
                .thenReturn(preguntas);

        String vista = juegoController.iniciarJuego(1, "easy", model);

        assertEquals("juego", vista);

        verify(model).addAttribute("totalPreguntas", 1);
        verify(model).addAttribute("preguntaNumero", 1);
        verify(model).addAttribute("pregunta", pregunta);
    }

    @Test
    public void cuandoOcurreUnaExcepcionRetornaVistaError() {

        when(preguntaService.obtenerPreguntas(1))
                .thenThrow(new RuntimeException("Error API"));

        String vista = juegoController.iniciarJuego(1, "", model);

        assertEquals("error", vista);

        verify(model).addAttribute(
                eq("error"),
                contains("Error API")
        );
    }

    @Test
    public void cuandoRespondeCorrectamenteYHayMasPreguntasContinuaJuego() {

        ApiPregunta pregunta1 = mock(ApiPregunta.class);
        ApiPregunta pregunta2 = mock(ApiPregunta.class);

        when(pregunta1.getRespuestaCorrectaDecodificada())
                .thenReturn("Java");

        List<ApiPregunta> preguntas =
                Arrays.asList(pregunta1, pregunta2);

        when(preguntaService.obtenerPreguntas(2))
                .thenReturn(preguntas);

        juegoController.iniciarJuego(2, "", model);

        String vista = juegoController.responder("Java", model);

        assertEquals("juego", vista);

        verify(model).addAttribute("preguntaNumero", 2);
        verify(model).addAttribute("pregunta", pregunta2);
        verify(model).addAttribute("puntajeActual", 1);
    }

    @Test
    public void cuandoTerminaElJuegoMuestraResultado() {

        ApiPregunta pregunta = mock(ApiPregunta.class);

        when(pregunta.getRespuestaCorrectaDecodificada())
                .thenReturn("Java");

        List<ApiPregunta> preguntas = Arrays.asList(pregunta);

        when(preguntaService.obtenerPreguntas(1))
                .thenReturn(preguntas);

        juegoController.iniciarJuego(1, "", model);

        clearInvocations(model);

        String vista = juegoController.responder("Java", model);

        assertEquals("resultado", vista);

        verify(model).addAttribute("puntajeFinal", 1);
        verify(model).addAttribute("totalPreguntas", 1);
        verify(model).addAttribute("porcentaje", 100);
    }
}
