package com.tallerwebi.presentacion.pregunta;

import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.categoriaDia.CategoriaEnum;
import com.tallerwebi.dominio.pregunta.Pregunta;
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

    if (pregunta.getOpcionA() == null || pregunta.getOpcionA().trim().isEmpty() ||
      pregunta.getOpcionB() == null || pregunta.getOpcionB().trim().isEmpty() ||
      pregunta.getOpcionC() == null || pregunta.getOpcionC().trim().isEmpty() ||
      pregunta.getOpcionD() == null || pregunta.getOpcionD().trim().isEmpty()) 
    {
      session.setAttribute("error", "Todas las opciones son obligatorias");
      return "redirect:/admin/mostrarCrearPregunta";
    }

    if (pregunta.getConsigna() == null || pregunta.getConsigna().trim().isEmpty()) {

      session.setAttribute("error","La consigna es obligatoria");
      return "redirect:/admin/mostrarCrearPregunta";
    }

    if (pregunta.getCategoria() == null || pregunta.getCategoria().trim().isEmpty()) {

      session.setAttribute("error", "La categoría es obligatoria");
      return "redirect:/admin/mostrarCrearPregunta";
    }

    if (pregunta.getCorrecta() == null || pregunta.getCorrecta().trim().isEmpty()) {

      session.setAttribute("error", "Debe indicar la respuesta correcta");
      return "redirect:/admin/mostrarCrearPregunta";
    }

    if (!respuestaValida(pregunta.getCorrecta())) {
      session.setAttribute(
        "error",
        "La respuesta correcta debe ser A, B, C o D"
      );
      return "redirect:/admin/mostrarCrearPregunta";
    }

    if (!categoriaValida(pregunta.getCategoria())) {
      
      session.setAttribute("error", "Categoría invalida");
      return "redirect:/admin/mostrarCrearPregunta";
    }
    // guardar en bd
    preguntaService.guardar(pregunta);

    // mensaje de éxito
    session.setAttribute("ok", "Pregunta creada correctamente");
    return "redirect:/admin/preguntas";
  }

  // Listar preguntas
  @RequestMapping(value = "/preguntas", method = RequestMethod.GET)
  public String listarPreguntas(HttpSession session, Model model) {

    // verificar admin
    if (!sessionUtil.verificarAdmin(session) ) {
      return "redirect:/login";
    }
    // traer preguntas de bd
    model.addAttribute("preguntas", preguntaService.listar());

    model.addAttribute("ok", session.getAttribute("ok"));
    model.addAttribute("error", session.getAttribute("error"));

    session.removeAttribute("ok");
    session.removeAttribute("error");

    return "redirect:/admin/preguntas";
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
      return "redirect:/admin/preguntas";
    }
    // buscar pregunta
    Pregunta pregunta = preguntaService.obtenerPorId(id);

    // verificar si existe
    if (pregunta == null) {
      session.setAttribute("error", "Pregunta no encontrada");
      return "redirect:/admin/preguntas";
    }

    // limpiar mensajes
    session.removeAttribute("ok");
    session.removeAttribute("error");
    model.addAttribute("pregunta", pregunta);

    return "admin/editarPregunta";
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

    return "redirect:/admin/preguntas";
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
      return "redirect:/admin/preguntas";
    }
    Pregunta pregunta = preguntaService.obtenerPorId(id);

    if (pregunta == null) {
      redirectAttributes.addFlashAttribute(
        "error", 
        "La pregunta no existe"
      );
      return "redirect:/admin/preguntas";    
    }

    // eliminar pregunta
    preguntaService.eliminar(id);
    // mensaje éxito
    redirectAttributes.addFlashAttribute("ok", "Pregunta eliminada correctamente");
    return "redirect:/admin/preguntas";
  }

  private boolean respuestaValida(String correcta) {
    return correcta != null && (correcta.equalsIgnoreCase("A") || 
      correcta.equalsIgnoreCase("B") || 
      correcta.equalsIgnoreCase("C") || 
      correcta.equalsIgnoreCase("D")
    );
  }

  private boolean categoriaValida(String categorias) {

    for (CategoriaEnum categoria : CategoriaEnum.values()) {
      if (categoria.name().equalsIgnoreCase(categorias)) {
        return true;
      }
    }

    return false;
  }

  
}