package com.tallerwebi.dominio.pregunta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PreguntaTest {
    private Pregunta pregunta;

    @BeforeEach
    public void init() {
        pregunta = new Pregunta();
    }

    @Test
    public void deberiaSetearYObtenerPregunta() {
        String textoPregunta = "¿Cuál es la capital de Argentina?";
        pregunta.setTextoPregunta(textoPregunta);

        assertEquals(textoPregunta, pregunta.getTextoPregunta());
    }

    @Test
    public void deberiaSetearYObtenerCorrecta() {
        String respuestaCorrecta = "Buenos Aires";
        pregunta.setCorrecta(respuestaCorrecta);

        assertEquals(respuestaCorrecta, pregunta.getCorrecta());
    }

    @Test
    public void deberiaSetearYObtenerOpcionA() {
        String opcion = "Buenos Aires";
        pregunta.setOpcionA(opcion);

        assertEquals(opcion, pregunta.getOpcionA());
    }

    @Test
    public void deberiaSetearYObtenerOpcionB() {
        String opcion = "Cordoba";
        pregunta.setOpcionB(opcion);

        assertEquals(opcion, pregunta.getOpcionB());
    }

    @Test
    public void deberiaSetearYObtenerOpcionC() {
        String opcion = "Rosario";
        pregunta.setOpcionC(opcion);

        assertEquals(opcion, pregunta.getOpcionC());
    }

    @Test
    public void deberiaSetearYObtenerOpcionD() {
        String opcion = "Mendoza";
        pregunta.setOpcionD(opcion);

        assertEquals(opcion, pregunta.getOpcionD());
    }

    @Test
    public void deberiaSetearYObtenerCategoria() {
        String categoria = "Geografia";
        pregunta.setCategoria(categoria);

        assertEquals(categoria, pregunta.getCategoria());
    }

    @Test
    public void deberiaSetearYObtenerTodosLosCampos() {

        pregunta.setTextoPregunta("Pregunta");
        pregunta.setCorrecta("A");
        pregunta.setOpcionA("Opcion A");
        pregunta.setOpcionB("Opcion B");
        pregunta.setOpcionC("Opcion C");
        pregunta.setOpcionD("Opcion D");
        pregunta.setCategoria("Historia");

        assertEquals("Pregunta", pregunta.getTextoPregunta());
        assertEquals("A", pregunta.getCorrecta());
        assertEquals("Opcion A", pregunta.getOpcionA());
        assertEquals("Opcion B", pregunta.getOpcionB());
        assertEquals("Opcion C", pregunta.getOpcionC());
        assertEquals("Opcion D", pregunta.getOpcionD());
        assertEquals("Historia", pregunta.getCategoria());
    }
}
