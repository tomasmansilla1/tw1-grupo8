package com.tallerwebi.presentacion.categoriaDia;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tallerwebi.config.SessionUtil;

@Controller
@RequestMapping("/admin")
public class ControladorCategoriaDia {

    @Autowired
    private SessionUtil sessionUtil;

    // Mostrar formulario
    @GetMapping("/categoriaDia")
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
        return "admin/categoriaDia";
    }

    // Guardar categoría
    @PostMapping("/categoriaDia")
    public String guardarCategoria(
        @RequestParam("categoria") String categoria,
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
        return "redirect:/admin";
    }
}