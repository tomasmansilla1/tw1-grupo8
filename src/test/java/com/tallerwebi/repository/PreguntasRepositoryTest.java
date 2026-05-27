package com.tallerwebi.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.tallerwebi.model.Pregunta;

@Transactional
public class PreguntasRepositoryTest {

    @Autowired
    private PreguntasRepository preguntasRepository;

    @Test
    @Rollback
    public void debeGuardarUnaPregunta() {
        // preparación
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Pregunta de prueba");

        preguntasRepository.save(pregunta);

        assertNotNull(pregunta.getId());
    }

    @Test
    @Rollback
    public void debeListarTodasLasPreguntas() {
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Pregunta test");

        preguntasRepository.save(pregunta);
        List<Pregunta> preguntas = preguntasRepository.findAll();

        assertFalse(preguntas.isEmpty());
    }

    @Test
    @Rollback
    public void debeBuscarPreguntaPorId() {
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Pregunta buscar");

        preguntasRepository.save(pregunta);
        Pregunta resultado = preguntasRepository.findById(pregunta.getId());

        assertNotNull(resultado);
        assertEquals("Pregunta buscar", resultado.getPregunta());
    }

    @Test
    @Rollback
    public void debeEliminarPreguntaPorId() {
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Pregunta eliminar");

        preguntasRepository.save(pregunta);
        Long id = pregunta.getId();
        preguntasRepository.deleteById(id);
        Pregunta eliminada = preguntasRepository.findById(id);

        assertNull(eliminada);
    }

    @Test
    @Rollback
    public void debeDevolverNullSiNoExistePregunta() {
        Pregunta resultado = preguntasRepository.findById(111L);
        assertNull(resultado);
    }

    @Test
    @Rollback
    public void noDebeRomperSiSeEliminaUnaPreguntaInexistente() {

        assertDoesNotThrow(
            () -> { preguntasRepository.deleteById(99L); }
        );
    }

    @Test
    @Rollback
    public void debeActualizarUnaPreguntaExistente() {
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Texto original");

        preguntasRepository.save(pregunta);
        pregunta.setPregunta("Texto actualizado");

        preguntasRepository.save(pregunta);
        Pregunta resultado = preguntasRepository.findById(pregunta.getId());

        assertEquals("Texto actualizado", resultado.getPregunta());
    }

    @Test
    @Rollback
    public void deberiaDevolverListaVaciaSiNoHayPreguntas() {
        List<Pregunta> preguntas = preguntasRepository.findAll();
        assertNotNull(preguntas);
    }

}