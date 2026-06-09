package com.tallerwebi.dominio.pregunta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ServicioPreguntaTest {

  // obj simulado para no usar bd real
  @Mock
  private RepositorioPreguntas repository;

  @InjectMocks
  private PreguntasService preguntasService;

  private Pregunta pregunta;

  @BeforeEach
  public void init() {
    // Inicializa mocks
    MockitoAnnotations.openMocks(this);

    pregunta = new Pregunta();
    pregunta.setId(1L);
  }

  @Test
  public void debeGuardarUnaPregunta() {
    preguntasService.guardar(pregunta);
    verify(repository, times(1)).save(pregunta);
  }

  @Test
  public void debeListarTodasLasPreguntas() {
    Pregunta pregunta2 = new Pregunta();
    pregunta2.setId(2L);
    List<Pregunta> preguntas = Arrays.asList(pregunta, pregunta2);

    when(repository.findAll()).thenReturn(preguntas);

    List<Pregunta> resultado = preguntasService.listar();

    assertNotNull(resultado);
    assertEquals(2, resultado.size());
    verify(repository, times(1)).findAll();
  }

  @Test
  public void deberiaObtenerPreguntaPorId() {
    // service cree que hablo con el repository real, pero hablo con el mock
    when(repository.findById(1L)).thenReturn(pregunta);

    Pregunta resultado = preguntasService.obtenerPorId(1L);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    verify(repository, times(1)).findById(1L);
  }

  @Test
  public void debeEliminarPreguntaPorId() {
    preguntasService.eliminar(1L);
    verify(repository, times(1)).deleteById(1L);
  }

  @Test
  public void debeDevolverNullCuandoNoExisteElId() {
    when(repository.findById(1L)).thenReturn(null);
    Pregunta resultado = preguntasService.obtenerPorId(1L);

    assertNull(resultado);
  }
}
