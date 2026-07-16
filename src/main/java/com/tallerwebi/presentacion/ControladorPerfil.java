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
import javax.servlet.http.HttpSession;

@Controller
public class ControladorPerfil {


    private final RepositoryUsuario repositoryUsuario;


    @Autowired
    public ControladorPerfil(RepositoryUsuario repositoryUsuario) {

        this.repositoryUsuario = repositoryUsuario;

    }

    @RequestMapping(
        path = "/perfil",
        method = RequestMethod.GET
    )
    public ModelAndView irAPerfil(HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return new ModelAndView( "redirect:/login" );
        }

        ModelMap model = new ModelMap();

        model.put(
            "usuario",
            usuario
        );

        return new ModelAndView(
            "perfil",
            model
        );

    }

    @RequestMapping(
        path = "/perfil-usuario",
        method = RequestMethod.GET
    )
    public ModelAndView verPerfilOtro(@RequestParam("id") Long id) {

        Usuario usuario = repositoryUsuario.buscar(id);

        if (usuario == null) {
            return new ModelAndView(
                "redirect:/ranking"
            );
        }

        ModelMap model = new ModelMap();

        model.put(
            "usuario",
            usuario
        );

        return new ModelAndView(
            "perfil",
            model
        );
    }

}