package com.tallerwebi.presentacion.pregunta;

import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.pregunta.Pregunta;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.pregunta.PreguntaService;


@Controller
@RequestMapping ("/admin")
public class ControllerPregunta {

  private PreguntaService preguntaService;
  private SessionUtil sessionUtil;

  @Autowired
  public ControllerPregunta(PreguntaService preguntaService, SessionUtil sessionUtil) {
    this.preguntaService = preguntaService;
    this.sessionUtil = sessionUtil;
  }

  // Mostrar formulario para crear pregunta
  @RequestMapping(value = "/mostrarCrearPregunta", method = RequestMethod.GET)
  public String mostrarFormulario (HttpSession session, Model model) {

    // verificar admin
    if (!sessionUtil.verificarAdmin(session) ) {
      return "redirect:/login";
    }
    model.addAttribute("pregunta", new Pregunta());

    return "admin/crearPregunta";
  }

  // Guardar pregunta
  @RequestMapping(value = "/guardarCrearPregunta", method = RequestMethod.POST)
  public String guardarPregunta(@ModelAttribute Pregunta pregunta, HttpSession session) {

    // verificar admin
    if (!sessionUtil.verificarAdmin(session) ) {
      return "redirect:/login";
    }
    // guardar en bd
    preguntaService.guardar(pregunta);

    // mensaje de éxito
    session.setAttribute("ok", "Pregunta creada correctamente");
    return "redirect:/admin/pregunta";
  }

  // Listar preguntas
  @RequestMapping(value = "/preguntas", method = RequestMethod.GET)
  public String listarPreguntas(HttpSession session, Model model) {

    // verificar admin
    if (!sessionUtil.verificarAdmin(session) ) {
      return "redirect:/login";
    }
    // traer preguntas de bd
    List<Pregunta> preguntas = preguntaService.listar();
    model.addAttribute("pregunta", preguntas);

    return "admin/pregunta";
  }

  // Mostrar form editar
  @RequestMapping(value = "/editarPregunta", method = RequestMethod.GET)
  public String editarPregunta(
    @RequestParam(required = false) Long id,
    HttpSession session, Model model) 
  {
    // verificar admin
    if (!sessionUtil.verificarAdmin(session) ) {
      return "redirect:/login";
    }
    // validar id
    if (id == null) {
      session.setAttribute("error", "ID erroneo");
      return "redirect:/admin/pregunta";
    }
    // buscar pregunta
    Pregunta pregunta = preguntaService.obtenerPorId(id);

    // verificar si existe
    if (pregunta == null) {
      session.setAttribute("error", "Pregunta no encontrada");
      return "redirect:/admin/pregunta";
    }

    // limpiar mensajes
    session.removeAttribute("ok");
    session.removeAttribute("error");
    model.addAttribute("pregunta", pregunta);

    return "admin/preguntas";
  }

  // Actualizar pregunta
  @RequestMapping(value = "/editarPregunta", method = RequestMethod.POST)
  public String actualizarPregunta(@ModelAttribute Pregunta pregunta, HttpSession session) {

    // verificar admin
    if (!sessionUtil.verificarAdmin(session) ) {
      return "redirect:/login";
    }
    // actualizar en bd
    preguntaService.guardar(pregunta);

    // mensaje éxito
    session.setAttribute("ok", "Pregunta actualizada");
    session.removeAttribute("ok");

    return "redirect:/admin/pregunta";
  }

  // Eliminar pregunta
  @RequestMapping(value = "/eliminarPregunta", method = RequestMethod.POST)
  public String eliminarPregunta(
    @RequestParam(required = false) Long id,
    HttpSession session,
    RedirectAttributes redirectAttributes) 
  {
    // verificar admin
    if (!sessionUtil.verificarAdmin(session)) {
      return "redirect:/login";
    }
    // validar id
    if (id == null) {
      redirectAttributes.addFlashAttribute("error", "ID inválido");
      return "redirect:/admin/pregunta";
    }
    Pregunta pregunta = preguntaService.obtenerPorId(id);

    if (pregunta == null) {
      redirectAttributes.addFlashAttribute(
        "error", 
        "La pregunta no existe"
      );
      return "redirect:/admin/pregunta";    
    }

    // eliminar pregunta
    preguntaService.eliminar(id);
    // mensaje éxito
    redirectAttributes.addFlashAttribute("ok", "Pregunta eliminada correctamente");
    return "redirect:/admin/pregunta";
  }

}