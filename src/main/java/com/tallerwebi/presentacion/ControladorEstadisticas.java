package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RankingTiempo;
import com.tallerwebi.dominio.ServicioEstadisticas;
import com.tallerwebi.dominio.excepcion.ListaUsuariosVaciaException;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class ControladorEstadisticas {

    ServicioEstadisticas servicioEstadisticas;

    @Autowired
    public ControladorEstadisticas(ServicioEstadisticas servicioEstadisitcas) {
        this.servicioEstadisticas = servicioEstadisitcas;
    }

    @RequestMapping(path = "/estadisticas", method = RequestMethod.GET)
    public ModelAndView vistasEstadisticas() {
        return new ModelAndView("menu-estadisticas");
    }

    @RequestMapping(path = "/estadisticas/categorias", method = RequestMethod.GET)
    public ModelAndView estadisticasCategoria() {
        ModelMap model = new ModelMap();

        List<Partida> listaPartidas = servicioEstadisticas.buscarTodasPartidasCategorias();

        Map<String, Integer> cantidadPorCategoria = servicioEstadisticas.filtrarCantidadPorCategoria(listaPartidas);

        model.put("listaCategorias", cantidadPorCategoria);
        return new ModelAndView("categoria", model);
    }

    @RequestMapping(path = "/estadisticas/tiempo", method = RequestMethod.GET)
    public ModelAndView esatdisticasTiempo() {
        ModelMap model = new ModelMap();

        List<Partida> partidasVictoriosas = servicioEstadisticas.obtenerPartidasVictoriosas();

        if (partidasVictoriosas.isEmpty()) {
            model.put("listaVacio", partidasVictoriosas);
            return new ModelAndView("tiempo", model);
        }

        List<RankingTiempo> usuariosTiempo = servicioEstadisticas.usuariosConMejorTiempo(partidasVictoriosas);

        model.put("usuariosTiempo", usuariosTiempo);
        return new ModelAndView("tiempo", model);
    }

    @RequestMapping(path = "/estadisticas/racha", method = RequestMethod.GET)
    public ModelAndView estadisticasRacha() {
        ModelMap model = new ModelMap();

        try {
            List<Usuario> listaUsuarios = servicioEstadisticas.usuariosConMejorRacha();
            model.put("usuariosRacha", listaUsuarios);
        }catch (ListaUsuariosVaciaException e) {
            model.put("error", e.getMessage());
            return new ModelAndView("racha", model);
        }

        return new ModelAndView("racha", model);
    }
}
