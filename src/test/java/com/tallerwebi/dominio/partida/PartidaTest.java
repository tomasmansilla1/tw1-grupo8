package com.tallerwebi.dominio.partida;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tallerwebi.dominio.usuario.Usuario;
import java.time.LocalDateTime;
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
      usuario
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
}
