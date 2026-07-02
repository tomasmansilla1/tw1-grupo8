package com.tallerwebi.dominio.partida;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tallerwebi.dominio.juego.Respuesta;
import com.tallerwebi.dominio.usuario.Usuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PartidaTest {

  @Test
  public void alCrearUnaPartidaDeberiaPoderSetearSusValores() {
    // Preparación (Dado)
    Partida partida = new Partida();
    LocalDateTime fecha = LocalDateTime.now();
    Usuario usuario = new Usuario();
    usuario.setEmail("test@test.com");

    // Ejecución (Cuando)
    partida.setPuntajeObtenido(100);
    partida.setEsVictoria(true);
    partida.setFecha(fecha);
    partida.setUsuario(usuario);

    // Validación (Entonces)
    assertThat(partida.getPuntajeObtenido(), is(equalTo(100)));
    assertThat(partida.getEsVictoria(), is(equalTo(true)));
    assertThat(partida.getFecha(), is(equalTo(fecha)));
    assertThat(partida.getUsuario().getEmail(), is(equalTo("test@test.com")));
  }

  @Test
  public void deberiaCrearPartidaConConstructor() {

    LocalDateTime fecha = LocalDateTime.now();
    Usuario usuario = new Usuario();

    Partida partida = new Partida(
      fecha,
      100,
      true,
      usuario, null
    );

    assertEquals(fecha, partida.getFecha());
    assertEquals(100, partida.getPuntajeObtenido());
    assertTrue(partida.getEsVictoria());
    assertEquals(usuario,partida.getUsuario());
  }

  @Test
  public void deberiaAsignarIdCorrectamente() {
    Partida partida = new Partida();
    partida.setId(1L);

    assertEquals(1L,partida.getId());
  }

  @Test
  public void debeGuardarYObtenerRespuesta() {

    Partida partida = new Partida();
    Respuesta respuesta = new Respuesta();

    partida.setRespuesta(respuesta);

    assertEquals(respuesta, partida.getRespuesta());
  }

  @Test
  public void debeGuardarYObtenerUsuario() {

    Partida partida = new Partida();
    Usuario usuario = new Usuario();

    partida.setUsuario(usuario);

    assertEquals(usuario, partida.getUsuario());
  }

  @Test
  public void debeGuardarYObtenerFecha() {

    Partida partida = new Partida();
    LocalDateTime fecha = LocalDateTime.now();

    partida.setFecha(fecha);

    assertEquals(fecha, partida.getFecha());
  }

  @Test
  public void debeGuardarYObtenerPuntaje() {

    Partida partida = new Partida();
    partida.setPuntajeObtenido(150);

    assertEquals(150, partida.getPuntajeObtenido());
  }

  @Test
  public void debeGuardarYObtenerVictoria() {

    Partida partida = new Partida();
    partida.setEsVictoria(true);

    assertTrue(partida.getEsVictoria());
  }

  @Test
  public void debeCrearPartidaConConstructorCompleto() {
    LocalDateTime fecha = LocalDateTime.now();
    Usuario usuario = new Usuario();
    Respuesta respuesta = new Respuesta();

    Partida partida = new Partida(
      fecha,
      200,
      true,
      usuario,
      respuesta
    );

    assertEquals(fecha, partida.getFecha());
    assertEquals(200, partida.getPuntajeObtenido());
    assertTrue(partida.getEsVictoria());
    assertEquals(usuario, partida.getUsuario());
    assertEquals(respuesta, partida.getRespuesta());
  }

  @Test
  public void debeObtenerYAsignarIdCorrectamenteEnRespuesta() {
    Respuesta respuesta = new Respuesta();

    respuesta.setId(1L);
    assertEquals(1L, respuesta.getId());
  }

  @Test
  public void debeObtenerYAsignarListaDeRespuestasCorrectamente() {
    Respuesta respuesta = new Respuesta();

    List<String> respuestas = new ArrayList<>();
    respuestas.add("A");
    respuestas.add("B");

    respuesta.setRespuestasUsuario(respuestas);

    assertEquals(respuestas, respuesta.getRespuestasUsuario());
    assertEquals(2, respuesta.getRespuestasUsuario().size());
  }
}
