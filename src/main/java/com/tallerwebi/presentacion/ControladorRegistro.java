package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioRegistro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

  ServicioRegistro servicioRegistro;

  @Autowired
  public ControladorRegistro(ServicioRegistro servicioRegistro) {
    this.servicioRegistro = servicioRegistro;
  }

  @RequestMapping("/formulario-registro-jugador")
  public String formularioRegistroJugador() {
    return "formulario-registro-jugador";
  }

  @RequestMapping("/iniciar-sesion")
  public ModelAndView irALogin() {
    return new ModelAndView("iniciar-sesion");
  }

  @RequestMapping("/registro")
  public ModelAndView registrar(
    @ModelAttribute("datosFormulario") DatosRegistroDTO datosRegistroDTO
  ) {
    ModelMap modelo = new ModelMap();

    try {
      servicioRegistro.registrar(
        datosRegistroDTO.getEmail(),
        datosRegistroDTO.getUsername(),
        datosRegistroDTO.getPassword()
      );
    } catch (IllegalArgumentException e) {
      modelo.put("error", e.getMessage());
      return new ModelAndView("formulario-registro-jugador", modelo);
    }

    modelo.put("mensaje", "Registro exitoso");

    return new ModelAndView("redirect:/iniciar-sesion");
  }

  @RequestMapping("/inicio")
  public ModelAndView iniciarSesion(
    @ModelAttribute("datosFormulario") DatosRegistroDTO datosRegistroDTO
  ) {
    ModelMap modelo = new ModelMap();

    try {
      servicioRegistro.iniciarSesion(datosRegistroDTO.getEmail(), datosRegistroDTO.getPassword());
    } catch (IllegalArgumentException e) {
      modelo.put("error", e.getMessage());
      return new ModelAndView("iniciar-sesion", modelo);
    }

    modelo.put("mensaje", "Inicio de sesion exitoso");

    return new ModelAndView("redirect:/home");
  }
}
