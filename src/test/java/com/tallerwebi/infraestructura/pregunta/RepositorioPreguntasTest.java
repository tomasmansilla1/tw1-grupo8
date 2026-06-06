package com.tallerwebi.infraestructura.pregunta;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.dominio.pregunta.RepositoryPreguntas;
import com.tallerwebi.integracion.config.HibernateInfraestructuraTestConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HibernateInfraestructuraTestConfig.class)
@Transactional
@Rollback
public class RepositorioPreguntasTest {

    @Autowired
    private RepositoryPreguntas preguntasRepository;

    @Test
    @Transactional
    @Rollback
    public void debeGuardarUnaPregunta() {
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Pregunta de prueba");

        pregunta.setCategoria("General");
        pregunta.setA("A");
        pregunta.setB("B");
        pregunta.setC("C");
        pregunta.setD("D");
        pregunta.setCorrecta("A");

        preguntasRepository.save(pregunta);
        assertNotNull(pregunta.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void debeBuscarPreguntaPorId() { 

        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Pregunta buscar");
        pregunta.setCategoria("General");
        pregunta.setA("A");
        pregunta.setB("B");
        pregunta.setC("C");
        pregunta.setD("D");
        pregunta.setCorrecta("A");

        preguntasRepository.save(pregunta);
        assertNotNull(pregunta.getId());

        Pregunta resultado = preguntasRepository.findById(pregunta.getId());

        assertNotNull(resultado);
        assertEquals("Pregunta buscar", resultado.getPregunta());
    }

    @Test
    @Rollback
    public void debeDevolverNullSiNoExistePregunta() {
        Pregunta resultado = preguntasRepository.findById(111L);
        assertNull(resultado);
    }

    @Test
    @Transactional
    @Rollback
    public void debeEliminarPreguntaPorId() {
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Pregunta eliminar");

        pregunta.setCategoria("General");
        pregunta.setA("A");
        pregunta.setB("B");
        pregunta.setC("C");
        pregunta.setD("D");
        pregunta.setCorrecta("A");

        preguntasRepository.save(pregunta);

        Long id = pregunta.getId();
        preguntasRepository.deleteById(id);
        Pregunta eliminada = preguntasRepository.findById(id);

        assertNull(eliminada);
    }

    @Test
    @Rollback
    public void noDebeRomperSiSeEliminaUnaPreguntaInexistente() {

        assertDoesNotThrow(
            () -> { preguntasRepository.deleteById(999L); }
        );
    }

    @Test
    @Transactional
    @Rollback
    public void debeActualizarUnaPreguntaExistente() {
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Texto original");

        pregunta.setCategoria("General");
        pregunta.setA("A");
        pregunta.setB("B");
        pregunta.setC("C");
        pregunta.setD("D");
        pregunta.setCorrecta("A");

        preguntasRepository.save(pregunta);
        pregunta.setPregunta("Texto actualizado");

        preguntasRepository.save(pregunta);
        Pregunta resultado = preguntasRepository.findById(pregunta.getId());

        assertEquals("Texto actualizado", resultado.getPregunta());
    }
  
    @Test
    @Transactional
    @Rollback
    public void debeListarTodasLasPreguntas() {
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Pregunta test");

        pregunta.setCategoria("General");
        pregunta.setA("A");
        pregunta.setB("B");
        pregunta.setC("C");
        pregunta.setD("D");
        pregunta.setCorrecta("A");

        preguntasRepository.save(pregunta);
        List<Pregunta> preguntas = preguntasRepository.findAll();

        assertFalse(preguntas.isEmpty());

        boolean existe = preguntas.stream()
        .anyMatch(p -> p.getPregunta().equals("Pregunta test"));

        assertTrue(existe);
    }

    @Test
    @Rollback
    public void deberiaDevolverListaVaciaSiNoHayPreguntas() {
        List<Pregunta> preguntas = preguntasRepository.findAll();

        assertNotNull(preguntas);
        assertTrue(preguntas.isEmpty());

    }

    @Test
    @Rollback
    public void noDebeEliminarNadaSiLaPreguntaNoExiste() {
        assertDoesNotThrow(() -> preguntasRepository.deleteById(Long.MAX_VALUE));
    }
}