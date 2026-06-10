package com.tallerwebi.presentacion.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tallerwebi.config.SessionUtil;

@Controller
@RequestMapping("/admin")
public class ControllerAdmin {

    private SessionUtil sessionUtil;

    @Autowired
    public ControllerAdmin(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String panelAdmin(HttpSession session, Model model) {

        // verificar admin
        if (!sessionUtil.verificarAdmin(session)) {
            return "redirect:/login";
        }

        // mensaje principal
        model.addAttribute("mensaje", "Bienvenido administrador");

        // mensajes de validacion o error
        Object ok = session.getAttribute("ok");
        Object error = session.getAttribute("error");

        if (ok != null) {
            model.addAttribute("ok", ok);

             // Se limpia de la sesión una vez usado
            session.removeAttribute("ok"); 
        }
        if (error != null) {
            model.addAttribute("error", error);

            // Se limpia de la sesión una vez usado
            session.removeAttribute("error"); 
        }

        // Devuelve: WEB-INF/views/thymeleaf/admin/panelAdmin.html
        return "admin/panelAdmin"; 
    }

    // MAPEO DE TODOS LOS BOTONES TH:HREF
    @RequestMapping(value = "/pregunta", method = RequestMethod.GET)
    public String gestionarPreguntas(HttpSession session) {

        if (!sessionUtil.verificarAdmin(session)) {
            return "redirect:/login";
        }
        return "admin/pregunta";
    }

    @RequestMapping(value = "/crearPregunta", method = RequestMethod.GET)
    public String crearPregunta(HttpSession session) {

        if (!sessionUtil.verificarAdmin(session)) {
            return "redirect:/login";
        }
        return "admin/crearPregunta";
    }

    @RequestMapping(value = "/categoriaDia", method = RequestMethod.GET)
    public String categoriaDia(HttpSession session) {

        if (!sessionUtil.verificarAdmin(session)) {
            return "redirect:/login";
        }
        return "admin/categoriaDia";
    }

    // hacer de la API
}