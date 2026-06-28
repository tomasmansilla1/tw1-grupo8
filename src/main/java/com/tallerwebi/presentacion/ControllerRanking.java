package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ranking.RankingService;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
public class ControllerRanking {

    private RankingService rankingService;

    @Autowired
    public ControllerRanking(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @RequestMapping(path = "/ranking", method = RequestMethod.GET)
    public ModelAndView irARanking() {
        ModelMap modelo = new ModelMap();
        List<Usuario> top10 = rankingService.obtenerTop10();
        modelo.put("listaUsuarios", top10);
        return new ModelAndView("ranking", modelo);
    }
}