package com.tallerwebi.presentacion.registro;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import com.tallerwebi.dominio.registro.DatosRegistroDTO;
import com.tallerwebi.dominio.registro.ServicioRegistro;
import com.tallerwebi.dominio.registro.ServicioRegistroImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorRegistroTest {

  ServicioRegistro servicioRegistro = new ServicioRegistroImpl(null);
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
      equalToIgnoringCase("redirect:/login")
    );
  }


  @Test
  void formRegistroJugadorDeberiaRetornarVistaCorrecta() {
    ModelAndView vista = controladorRegistro.formularioRegistroJugador();

    assertEquals("formulario-registro-jugador", vista);
  }


}
