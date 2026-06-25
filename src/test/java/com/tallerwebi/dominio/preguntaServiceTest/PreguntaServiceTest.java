package com.tallerwebi.dominio.preguntaServiceTest;

import com.tallerwebi.dominio.apiPregunta.ApiPregunta;
import com.tallerwebi.dominio.apiResponse.ApiResponse;
import com.tallerwebi.dominio.servicioPregunta.PreguntaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PreguntaServiceTest {

    private PreguntaService preguntaService;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void init() throws Exception {

        preguntaService = new PreguntaService();

        restTemplateMock = mock(RestTemplate.class);

        Field field = PreguntaService.class.getDeclaredField("restTemplate");
        field.setAccessible(true);
        field.set(preguntaService, restTemplateMock);
    }

    @Test
    void deberiaObtenerPreguntasCorrectamente() {

        ApiPregunta pregunta = new ApiPregunta();
        pregunta.setQuestion("What is Java?");
        pregunta.setCorrectAnswer("Programming Language");
        pregunta.setIncorrectAnswers(
                new ArrayList<>(List.of(
                        "Database",
                        "Browser",
                        "Operating System"
                ))
        );

        ApiResponse response = new ApiResponse();
        response.setResponseCode(0);
        response.setResults(List.of(pregunta));

        when(restTemplateMock.getForObject(
                contains("opentdb.com"),
                eq(ApiResponse.class)
        )).thenReturn(response);

        when(restTemplateMock.getForObject(
                contains("translated.net"),
                eq(Map.class)
        )).thenReturn(
                Map.of(
                        "responseData",
                        Map.of(
                                "translatedText",
                                "Texto traducido"
                        )
                )
        );

        List<ApiPregunta> resultado =
                preguntaService.obtenerPreguntas(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Texto traducido",
                resultado.get(0).getQuestion()
        );
    }

    @Test
    void deberiaLanzarExcepcionCuandoLaApiFalla() {

        ApiResponse response = new ApiResponse();
        response.setResponseCode(1);

        when(restTemplateMock.getForObject(
                anyString(),
                eq(ApiResponse.class)
        )).thenReturn(response);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> preguntaService.obtenerPreguntas(5)
        );

        assertEquals(
                "No se pudieron obtener las preguntas",
                exception.getMessage()
        );
    }

    @Test
    void deberiaObtenerPreguntasPorDificultad() {

        ApiPregunta pregunta = new ApiPregunta();
        pregunta.setQuestion("Question");

        ApiResponse response = new ApiResponse();
        response.setResponseCode(0);
        response.setResults(List.of(pregunta));

        when(restTemplateMock.getForObject(
                contains("difficulty=easy"),
                eq(ApiResponse.class)
        )).thenReturn(response);

        when(restTemplateMock.getForObject(
                contains("translated.net"),
                eq(Map.class)
        )).thenReturn(
                Map.of(
                        "responseData",
                        Map.of(
                                "translatedText",
                                "Traducido"
                        )
                )
        );

        List<ApiPregunta> resultado =
                preguntaService.obtenerPreguntasPorDificultad(
                        1,
                        "easy"
                );

        assertFalse(resultado.isEmpty());
    }
}