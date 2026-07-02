package com.tallerwebi.presentacion.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.categoriaDia.CategoriaService;
import com.tallerwebi.dominio.pregunta.PreguntaService;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.ServicioUsuario;


@Controller
@RequestMapping("/admin")
public class ControllerAdmin {

    private SessionUtil sessionUtil;
    private PreguntaService preguntaService;
    private CategoriaService categoriaService;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControllerAdmin(
        SessionUtil sessionUtil,
        PreguntaService preguntaService,
        CategoriaService categoriaService,
        RepositoryUsuario repositoryUsuario,
        ServicioUsuario servicioUsuario)
    {
        this.sessionUtil = sessionUtil;
        this.preguntaService = preguntaService;
        this.categoriaService = categoriaService;
        this.servicioUsuario = servicioUsuario;
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
        return servicioUsuario.listarTodos().size();
    }

    @RequestMapping(value="/usuarios", method=RequestMethod.GET)
    public String usuarios(HttpSession session, Model model){

        if(!sessionUtil.verificarAdmin(session)){
            return "redirect:/login";
        }

        model.addAttribute("usuarios",
            servicioUsuario.listarJugadores());

        return "admin/usuarios";
    }

    @RequestMapping(value="/ban1/{id}", method=RequestMethod.POST)
    public String ban1(@PathVariable Long id){

        servicioUsuario.banear(id,1,"Suspendido por administrador");

        return "redirect:/admin/usuarios";
    }

    @RequestMapping(value="/ban7/{id}", method=RequestMethod.POST)
    public String ban7(@PathVariable Long id){

        servicioUsuario.banear(id,7,"Suspendido por administrador");

        return "redirect:/admin/usuarios";
    }

    @RequestMapping(value="/banpermanente/{id}", method=RequestMethod.POST)
    public String banPermanente(@PathVariable Long id){

        servicioUsuario.banPermanente(id,"Ban permanente");

        return "redirect:/admin/usuarios";
    }

    @RequestMapping(value="/desban/{id}", method=RequestMethod.POST)
    public String desbanear(@PathVariable Long id){

        servicioUsuario.desbanear(id);

        return "redirect:/admin/usuarios";
    }

    @RequestMapping(value="/advertir/{id}", method=RequestMethod.POST)
    public String advertir(@PathVariable Long id){

        servicioUsuario.advertir(id);

        return "redirect:/admin/usuarios";
    }
}