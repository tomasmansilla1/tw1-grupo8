package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RankingService;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
public class ControladorRanking {

    RankingService rankingService;

    @Autowired
    public ControladorRanking(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @RequestMapping(path = "/rankings", method = RequestMethod.GET)
    public ModelAndView verRankings() {
        ModelMap model = new ModelMap();
        model.put("categoria", new String());
        return new ModelAndView("elegir-ranking", model);
    }

    @RequestMapping(path = "/ranking-elegido", method = RequestMethod.POST)
    public ModelAndView rankingElegido(@RequestParam String categoria) {
        ModelMap model = new ModelMap();

        Set<Usuario> listaDeUsuarioPorRanking = rankingService.buscarUsuariosPorRanking(categoria);
        model.put("usuarios", listaDeUsuarioPorRanking);
        model.put("users", "usuarios encontrados");

        return new ModelAndView("ranking-categoria", model);
    }
}
