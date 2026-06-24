package com.tallerwebi.presentacion.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.categoriaDia.CategoriaService;
import com.tallerwebi.dominio.pregunta.PreguntaService;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;


@Controller
@RequestMapping("/admin")
public class ControllerAdmin {

    private SessionUtil sessionUtil;
    private PreguntaService preguntaService;
    private CategoriaService categoriaService;
    private RepositoryUsuario repositoryUsuario;

    @Autowired
    public ControllerAdmin(
        SessionUtil sessionUtil,
        PreguntaService preguntaService,
        CategoriaService categoriaService,
        RepositoryUsuario repositoryUsuario) 
    {
        this.sessionUtil = sessionUtil;
        this.preguntaService = preguntaService;
        this.categoriaService = categoriaService;
        this.repositoryUsuario = repositoryUsuario;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String panelAdmin(HttpSession session, Model model) {

        // verificar admin
        if (!sessionUtil.verificarAdmin(session)) {
            return "redirect:/login";
        }

        // mensaje principal
        model.addAttribute("mensaje", "Bienvenido administrador");

        model.addAttribute("cantidadPreguntas", preguntaService.listar().size());
        model.addAttribute("categoriaActual", categoriaService.obtenerCategoriaActiva());
        model.addAttribute("cantidadUsuarios", obtenerCantidadUsuarios());
        
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

    private Integer obtenerCantidadUsuarios() {
        return repositoryUsuario.listarTodos().size();
    }

    // hacer de la API
}