package com.tallerwebi.presentacion.categoriaDia;

import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.categoriaDia.CategoriaService;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class ControllerCategoriaDia {

  private SessionUtil sessionUtil;
  private CategoriaService categoriaService;

  @Autowired
  public ControllerCategoriaDia(SessionUtil sessionUtil, CategoriaService categoriaService) {
    this.sessionUtil = sessionUtil;
    this.categoriaService = categoriaService;
  }

  // Mostrar formulario
  @RequestMapping(value = "/categoriaDia", method = RequestMethod.GET)
  public String mostrarCategoriaDia(HttpSession session, Model model) {

    // verificar admin
    if (!sessionUtil.verificarAdmin(session)) {
      return "redirect:/login";
    }

    // enviar categoría actual a la vista
    model.addAttribute(
      "categoriaDia", 
      categoriaService.obtenerCategoriaActiva()
    );
    // enviar un historial a la vista
    model.addAttribute(
      "historial",
      categoriaService.obtenerHistorial()
    );

    model.addAttribute("ok",session.getAttribute("ok"));
    model.addAttribute("error",session.getAttribute("error"));

    // limpiar mensajes
    session.removeAttribute("ok");
    session.removeAttribute("error");

    return "admin/categoriaDia";
  }

  // Guardar categoría
  @RequestMapping(value = "/guardarCategoriaDia", method = RequestMethod.POST)
  public String guardarCategoria(@RequestParam("categoria") String categoria,HttpSession session) 
  {
    // verificar admin
    if (!sessionUtil.verificarAdmin(session)) {
      return "redirect:/login";
    }
    // validar categoría
    if (categoria == null || categoria.trim().isEmpty()) {
      
      session.setAttribute(
        "error",
        "Debe seleccionar una categoría"
      );

      return "redirect:/admin/categoriaDia";
    }

    categoriaService.guardarNuevaCategoria(categoria);

    // mensaje éxito
    session.setAttribute("ok", "Categoría actualizada correctamente");
       
    // Redirección limpia al panel principal de administración
    return "redirect:/admin/categoriaDia";
  }
  
}