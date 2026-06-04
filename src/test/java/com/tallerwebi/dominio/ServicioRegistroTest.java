package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ServicioRegistroTest {

  ServicioRegistro servicioRegistro = new ServicioRegistroImpl();
  String email = "tomas@gmail.com";
  String username = "tmansilla7";
  String password = "123456Ddw.";

  @Test
  public void siIngresoMailYPasswordValidosElRegistroEsExitoso() {
    // preparacion
    givenElUsuarioNoExiste();

    // ejecucion
    Jugador jugador = whenRegistroUsuario(email, username, password);

    // comprobacion
    thenElRegistroEsExitoso(jugador);
  }

  @Test
  public void siIngresoPasswordInvalidoElRegistroFalla() {
    givenElUsuarioNoExiste();

    assertThrows(
      IllegalArgumentException.class,
      () -> whenRegistroUsuario(email, username, "1234")
    );
  }

  private void givenElUsuarioNoExiste() {}

  private Jugador whenRegistroUsuario(String email, String username, String password) {
    Jugador jugador = servicioRegistro.registrar(email, username, password);
    return jugador;
  }

  private void thenElRegistroEsExitoso(Jugador jugador) {
    assertThat(jugador, is(notNullValue()));
  }
}
