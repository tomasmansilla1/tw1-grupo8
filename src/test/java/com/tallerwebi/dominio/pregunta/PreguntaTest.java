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
        pregunta.setConsigna(textoPregunta);

        assertEquals(textoPregunta, pregunta.getConsigna());
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

        pregunta.setConsigna("Pregunta");
        pregunta.setCorrecta("A");
        pregunta.setOpcionA("Opcion A");
        pregunta.setOpcionB("Opcion B");
        pregunta.setOpcionC("Opcion C");
        pregunta.setOpcionD("Opcion D");
        pregunta.setCategoria("Historia");

        assertEquals("Pregunta", pregunta.getConsigna());
        assertEquals("A", pregunta.getCorrecta());
        assertEquals("Opcion A", pregunta.getOpcionA());
        assertEquals("Opcion B", pregunta.getOpcionB());
        assertEquals("Opcion C", pregunta.getOpcionC());
        assertEquals("Opcion D", pregunta.getOpcionD());
        assertEquals("Historia", pregunta.getCategoria());
    }

    @Test
    public void debeCrearPreguntaConTodosLosDatos() {

        Pregunta pregunta = new Pregunta(
            "CIENCIA",
            "¿Cuál es el planeta más grande?",
            "Tierra",
            "Marte",
            "Júpiter",
            "Venus",
            "C"
        );

        assertEquals("CIENCIA", pregunta.getCategoria());
        assertEquals("¿Cuál es el planeta más grande?", pregunta.getConsigna());
        assertEquals("Tierra", pregunta.getOpcionA());
        assertEquals("Marte", pregunta.getOpcionB());
        assertEquals("Júpiter", pregunta.getOpcionC());
        assertEquals("Venus", pregunta.getOpcionD());
        assertEquals("C", pregunta.getCorrecta());
    }

    @Test
    public void debeGuardarYObtenerDatosDePregunta() {

        Pregunta pregunta = new Pregunta();

        pregunta.setCategoria("HISTORIA");
        pregunta.setConsigna("¿Quién descubrió América?");
        pregunta.setOpcionA("Colón");
        pregunta.setOpcionB("San Martín");
        pregunta.setOpcionC("Belgrano");
        pregunta.setOpcionD("Sarmiento");
        pregunta.setCorrecta("A");

        assertEquals("HISTORIA", pregunta.getCategoria());
        assertEquals("¿Quién descubrió América?", pregunta.getConsigna());
        assertEquals("Colón", pregunta.getOpcionA());
        assertEquals("San Martín", pregunta.getOpcionB());
        assertEquals("Belgrano", pregunta.getOpcionC());
        assertEquals("Sarmiento", pregunta.getOpcionD());
        assertEquals("A", pregunta.getCorrecta());
    }
}
