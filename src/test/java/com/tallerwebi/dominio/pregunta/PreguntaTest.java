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
        pregunta.setPregunta(textoPregunta);

        assertEquals(textoPregunta, pregunta.getPregunta());
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
        pregunta.setA(opcion);

        assertEquals(opcion, pregunta.getA());
    }

    @Test
    public void deberiaSetearYObtenerOpcionB() {
        String opcion = "Cordoba";
        pregunta.setB(opcion);

        assertEquals(opcion, pregunta.getB());
    }

    @Test
    public void deberiaSetearYObtenerOpcionC() {
        String opcion = "Rosario";
        pregunta.setC(opcion);

        assertEquals(opcion, pregunta.getC());
    }

    @Test
    public void deberiaSetearYObtenerOpcionD() {
        String opcion = "Mendoza";
        pregunta.setD(opcion);

        assertEquals(opcion, pregunta.getD());
    }

    @Test
    public void deberiaSetearYObtenerCategoria() {
        String categoria = "Geografia";
        pregunta.setCategoria(categoria);

        assertEquals(categoria, pregunta.getCategoria());
    }

    @Test
    public void deberiaSetearYObtenerTodosLosCampos() {

        pregunta.setPregunta("Pregunta");
        pregunta.setCorrecta("A");
        pregunta.setA("Opcion A");
        pregunta.setB("Opcion B");
        pregunta.setC("Opcion C");
        pregunta.setD("Opcion D");
        pregunta.setCategoria("Historia");

        assertEquals("Pregunta", pregunta.getPregunta());
        assertEquals("A", pregunta.getCorrecta());
        assertEquals("Opcion A", pregunta.getA());
        assertEquals("Opcion B", pregunta.getB());
        assertEquals("Opcion C", pregunta.getC());
        assertEquals("Opcion D", pregunta.getD());
        assertEquals("Historia", pregunta.getCategoria());
    }
}
