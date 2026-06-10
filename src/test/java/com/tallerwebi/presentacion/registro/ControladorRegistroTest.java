package com.tallerwebi.presentacion.registro;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import com.tallerwebi.dominio.registro.DatosRegistroDTO;
import com.tallerwebi.dominio.registro.ServicioRegistro;
import com.tallerwebi.dominio.registro.ServicioRegistroImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorRegistroTest {

  ServicioRegistro servicioRegistro = new ServicioRegistroImpl();
  ControladorRegistro controladorRegistro = new ControladorRegistro(servicioRegistro);

  private DatosRegistroDTO datos() {
    return new DatosRegistroDTO(
      "tomas@gmail.com",
      "tomas",
      "1234A.ddd"
    );
  }

  @Test
  public void siSeRegistraUsuarioRedirigeALogin() {
    ModelAndView modelAndView = controladorRegistro.registrar(datos());

    assertThat(
      modelAndView.getViewName(), 
      equalToIgnoringCase("redirect:/iniciar-sesion")
    );
  }


  @Test
  void siSeIniciaSesionCorrectamenteRedirigeAHome() {
    controladorRegistro.registrar(datos());

    ModelAndView modelAndView = controladorRegistro.iniciarSesion(datos());

    assertThat(
      modelAndView.getViewName(), 
      equalToIgnoringCase("redirect:/home")
    );
  }

  @Test
  void siLoginIncorrectoNoRedirigeAHome() {
    DatosRegistroDTO datos = new DatosRegistroDTO(
      "otro@mail.com",
      "otro",
      "1234A.ddd"
    );
    ModelAndView modelAndView = controladorRegistro.iniciarSesion(datos);

    assertNotEquals("redirect:/home", modelAndView.getViewName());
  }

  @Test
  void formRegistroJugadorDeberiaRetornarVistaCorrecta() {
    String vista = controladorRegistro.formularioRegistroJugador();

    assertEquals("formulario-registro-jugador", vista);
  }

  @Test
  void irALoginDeberiaRetornarVistaCorrecta() {
    ModelAndView modelAndView = controladorRegistro.irALogin();
    assertEquals("iniciar-sesion", modelAndView.getViewName());
  }
}
