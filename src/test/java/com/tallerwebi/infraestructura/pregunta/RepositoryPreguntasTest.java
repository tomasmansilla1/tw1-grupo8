package com.tallerwebi.infraestructura.pregunta;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HibernateInfraestructuraTestConfig.class)
@Transactional
@Rollback
public class RepositoryPreguntasTest {

  @Autowired
  private RepositoryPreguntas preguntasRepository;

  private Pregunta crearPregunta(String texto) {
    Pregunta pregunta = new Pregunta();

    pregunta.setTextoPregunta(texto);
    pregunta.setCategoria("General");
    pregunta.setOpcionA("A");
    pregunta.setOpcionB("B");
    pregunta.setOpcionC("C");
    pregunta.setOpcionD("D");
    pregunta.setCorrecta("A");

    return pregunta;
  }
  @Test
  @Transactional
  @Rollback
  public void debeGuardarUnaPregunta() {
    Pregunta pregunta = crearPregunta("Pregunta de prueba");

    preguntasRepository.save(pregunta);
    assertNotNull(pregunta.getId());
  }

  @Test
  @Transactional
  @Rollback
  public void debeBuscarPreguntaPorId() { 

    Pregunta pregunta = crearPregunta("Pregunta buscar");

    preguntasRepository.save(pregunta);
    assertNotNull(pregunta.getId());

    Pregunta resultado = preguntasRepository.findById(pregunta.getId());

    assertNotNull(resultado);
    assertEquals("Pregunta buscar", resultado.getTextoPregunta());
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
    Pregunta pregunta = crearPregunta("Pregunta eliminar");

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
    Pregunta pregunta = crearPregunta("Texto original");
    preguntasRepository.save(pregunta);

    pregunta.setTextoPregunta("Texto actualizado");
    preguntasRepository.save(pregunta);

    Pregunta resultado = preguntasRepository.findById(pregunta.getId());

    assertEquals("Texto actualizado", resultado.getTextoPregunta());
  }
  
  @Test
  @Transactional
  @Rollback
  public void debeListarTodasLasPreguntas() {
    Pregunta pregunta = crearPregunta("Pregunta test");

    preguntasRepository.save(pregunta);
    List<Pregunta> preguntas = preguntasRepository.findAll();

    assertNotNull(preguntas);

    assertTrue(
      preguntas.stream().anyMatch(
        p -> p.getTextoPregunta().equals(
          "Pregunta test"
        )
      )
    );
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
