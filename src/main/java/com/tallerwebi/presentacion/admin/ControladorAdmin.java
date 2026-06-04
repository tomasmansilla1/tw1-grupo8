package com.tallerwebi.presentacion.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tallerwebi.config.SessionUtil;

@Controller
public class ControladorAdmin {

    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping("/admin")
    public String panelAdmin(HttpSession session, Model model) {

        // verificar admin
        if (!sessionUtil.verificarAdmin(session)) {
            return "redirect:/login";
        }

        // mensaje principal
        model.addAttribute("mensaje", "Bienvenido administrador");

        // mensajes de sesión
        model.addAttribute("ok", session.getAttribute("ok"));
        model.addAttribute("error", session.getAttribute("error"));

        // limpiar mensajes
        session.removeAttribute("ok");
        session.removeAttribute("error");
        return "admin/panelAdmin";
    }
}