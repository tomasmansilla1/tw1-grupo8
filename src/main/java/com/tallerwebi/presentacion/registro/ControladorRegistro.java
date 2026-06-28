package com.tallerwebi.presentacion.registro;

import com.tallerwebi.dominio.registro.DatosRegistroDTO;
import com.tallerwebi.dominio.registro.ServicioRegistro;

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
  public ModelAndView formularioRegistroJugador() {
    ModelMap modelo = new ModelMap();
    modelo.put("datosFormulario", new DatosRegistroDTO());

    return new ModelAndView("formulario-registro-jugador", modelo);
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

    return new ModelAndView("redirect:/login");
  }

}
