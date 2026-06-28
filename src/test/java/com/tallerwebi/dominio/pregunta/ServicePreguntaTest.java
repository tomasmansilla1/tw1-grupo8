package com.tallerwebi.dominio.pregunta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicePreguntaTest {

  // obj simulado para no usar bd real
  // mock manual
  private RepositoryPreguntas repositoryMock;
  private PreguntaService preguntasService;
  private Pregunta pregunta;


  @BeforeEach
  public void init() {
    // crear mock manualmente
    this.repositoryMock = mock(RepositoryPreguntas.class);

    // inyectar mock en el service
    this.preguntasService = new PreguntaServiceImpl(repositoryMock);

    pregunta = new Pregunta();
    pregunta.setId(1L);
  }

  @Test
  public void debeGuardarUnaPregunta() {
    preguntasService.guardar(pregunta);
    verify(repositoryMock, times(1)).save(pregunta);
  }

  @Test
  public void debeListarTodasLasPreguntas() {
    Pregunta pregunta2 = new Pregunta();
    pregunta2.setId(2L);
    List<Pregunta> preguntas = Arrays.asList(pregunta, pregunta2);

    when(repositoryMock.findAll()).thenReturn(preguntas);

    List<Pregunta> resultado = preguntasService.listar();

    assertNotNull(resultado);
    assertEquals(2, resultado.size());
    verify(repositoryMock, times(1)).findAll();
  }

  @Test
  public void deberiaDevolverListaVacia() {
    when(repositoryMock.findAll()).thenReturn(Collections.emptyList());
    List<Pregunta> resultado = preguntasService.listar();

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
    verify(repositoryMock, times(1)).findAll();
  }

  @Test
  public void deberiaObtenerPreguntaPorId() {
    // service cree que hablo con el repository real, pero hablo con el mock
    when(repositoryMock.findById(1L)).thenReturn(pregunta);
    Pregunta resultado = preguntasService.obtenerPorId(1L);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    verify(repositoryMock, times(1)).findById(1L);
  }

  @Test
  public void debeEliminarPreguntaPorId() {
    preguntasService.eliminar(1L);
    verify(repositoryMock, times(1)).deleteById(1L);
  }
  @Test
  public void deberiaEliminarElIdCorrecto() {
    Long id = 25L;
    preguntasService.eliminar(id);
    verify(repositoryMock).deleteById(id);
  }

  @Test
  public void debeDevolverNullCuandoNoExisteElId() {
    when(repositoryMock.findById(1L)).thenReturn(null);
    Pregunta resultado = preguntasService.obtenerPorId(1L);

    assertNull(resultado);
  }

  @Test
  public void debeObtenerPreguntasPorCategoria() {

    String categoria = "CIENCIA";

    List<Pregunta> preguntasEsperadas = List.of(
      new Pregunta(
        "CIENCIA",
        "¿Cuál es el planeta más grande?",
        "Tierra",
        "Marte",
        "Júpiter",
        "Venus",
        "C"
        )
      );

    when(repositoryMock.buscarPorCategoria(categoria)).thenReturn(preguntasEsperadas);

    List<Pregunta> resultado = preguntasService.obtenerPorCategoria(categoria);

    assertEquals(1, resultado.size());
    assertEquals("CIENCIA", resultado.get(0).getCategoria());

    verify(repositoryMock).buscarPorCategoria(categoria);
  }
}
