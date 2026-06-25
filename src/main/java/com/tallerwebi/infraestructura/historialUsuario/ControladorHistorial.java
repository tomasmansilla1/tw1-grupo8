package com.tallerwebi.infraestructura.historialUsuario;

import javax.servlet.http.HttpServletRequest;

import com.tallerwebi.dominio.juego.ServicioJuego;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.usuario.Usuario;

@Controller
@RequestMapping("/usuario")
public class ControladorHistorial {

    private ServicioJuego servicioJuego;

    public ControladorHistorial(ServicioJuego servicioJuego) {
        this.servicioJuego = servicioJuego;
    }

    @RequestMapping(value = "/historial", method = RequestMethod.GET)
    public ModelAndView historial(HttpServletRequest request) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelMap model = new ModelMap();
        model.put("partidas", servicioJuego.buscarHistorial(usuario.getId()));

        return new ModelAndView("historial", model);
    }
}