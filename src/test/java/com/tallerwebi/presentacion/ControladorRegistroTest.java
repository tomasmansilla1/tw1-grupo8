package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import com.tallerwebi.dominio.ServicioRegistro;
import com.tallerwebi.dominio.ServicioRegistroImpl;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorRegistroTest {

  private final String email = "tomas@gmail.com";
  private final String username = "tomas";
  private final String password = "1234A.ddd";
  private final DatosRegistroDTO datosRegistroDTO = new DatosRegistroDTO(email, username, password);

  ServicioRegistro servicioRegistro = new ServicioRegistroImpl();
  ControladorRegistro controladorRegistro = new ControladorRegistro(servicioRegistro);

  @Test
  public void siSeIngresaEmailyPasswordElRegistroEsExitoso() {
    // preparación
    givenNoExisteUsuario();

    // ejecución
    ModelAndView modelAndView = whenRegistroUsuario(datosRegistroDTO);

    // verificación
    thenElRegistroEsExitoso(modelAndView);
  }

  @Test
  public void siSeIngresanEmailYPasswordYaRegistradosElInicioEsExitoso() {
    // preparacion
    givenUsuarioRegistrado();

    // ejecucion
    ModelAndView modelAndView = whenUsuarioIniciaSesion(datosRegistroDTO);

    // verificacion
    thenElInicioDeSesionEsExitoso(modelAndView);
  }

  private void givenUsuarioRegistrado() {
    controladorRegistro.registrar(datosRegistroDTO);
  }

  private ModelAndView whenUsuarioIniciaSesion(DatosRegistroDTO datosRegistroDTO) {
    ModelAndView modelAndView = controladorRegistro.iniciarSesion(datosRegistroDTO);
    return modelAndView;
  }

  private void thenElInicioDeSesionEsExitoso(ModelAndView modelAndView) {
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
  }

  private void givenNoExisteUsuario() {}

  private ModelAndView whenRegistroUsuario(DatosRegistroDTO datosRegistroDTO) {
    ModelAndView modelAndView = controladorRegistro.registrar(datosRegistroDTO);
    return modelAndView;
  }

  private void thenElRegistroEsExitoso(ModelAndView modelAndView) {
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/iniciar-sesion"));
  }
}
