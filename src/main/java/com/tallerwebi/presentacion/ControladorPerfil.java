package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPerfil {

    private RepositoryUsuario repositoryUsuario;

    @Autowired
    public ControladorPerfil(RepositoryUsuario repositoryUsuario) {
        this.repositoryUsuario = repositoryUsuario;
    }

    // Ruta para MI perfil (sin parámetros)
    @RequestMapping(path = "/perfil", method = RequestMethod.GET)
    public ModelAndView irAPerfil(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelMap model = new ModelMap();
        model.put("usuario", usuario);
        return new ModelAndView("perfil", model);
    }

    // Ruta para ver el perfil de OTRO (con ID)
    @RequestMapping(path = "/perfil-usuario", method = RequestMethod.GET)
    public ModelAndView verPerfilOtro(@RequestParam("id") Long id) {
        Usuario usuario = repositoryUsuario.buscar(id); // Asumiendo que tenés este método

        if (usuario == null) {
            return new ModelAndView("redirect:/ranking");
        }

        ModelMap model = new ModelMap();
        model.put("usuario", usuario);
        return new ModelAndView("perfil", model);
    }
}