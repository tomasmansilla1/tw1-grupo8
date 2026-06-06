package com.tallerwebi.presentacion.categoriaDia;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tallerwebi.config.SessionUtil;

@Controller
@RequestMapping("/admin")
public class ControllerCategoriaDia {

    private SessionUtil sessionUtil;

    @Autowired
    public ControllerCategoriaDia(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
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
            session.getAttribute("categoria_dia")
        );

       // Devuelve: WEB-INF/views/thymeleaf/admin/categoriaDia.html
        return "admin/categoriaDia";
    }

    // Guardar categoría
    @RequestMapping(value = "/categoriaDia", method = RequestMethod.POST)
    public String guardarCategoria(
        @RequestParam(value = "categoria", required = false) 
        String categoria,
        HttpSession session,
        Model model) 
    {
        // verificar admin
        if (!sessionUtil.verificarAdmin(session)) {
            return "redirect:/login";
        }
        // validar categoría
        if (categoria == null || categoria.trim().isEmpty()) {
            model.addAttribute(
                "error", 
                "Seleccionar una categoría"
            );
            return "admin/categoriaDia";
        }

        // guardar en sesión
        session.setAttribute("categoria_dia", categoria);

        // mensaje éxito
        session.setAttribute("ok", "La categoría está actualizada");
       
        // Redirección limpia al panel principal de administración
        return "redirect:/admin";
    }
}