package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RankingService;
import com.tallerwebi.dominio.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorRankingTest {

    private ControladorRanking controladorRanking;
    private RankingService rankingServiceMock;

    @BeforeEach
    public void init() {
        rankingServiceMock = mock(RankingService.class);
        controladorRanking = new ControladorRanking(rankingServiceMock);
    }
    @Test
    public void queAlAccederARankingsRetornaLaVistaCorrecta() {
        ModelAndView model = controladorRanking.verRankings();

        assertThat(model.getViewName(), equalToIgnoringCase("elegir-ranking"));
    }

    @Test
    public void queAlElegirCategoriaRetornaVistaRankingCategoria() {
        String categoria = "historia";
        Set<Usuario> usuarios = new TreeSet<>();

        when(rankingServiceMock.buscarUsuariosPorRanking(categoria)).thenReturn(usuarios);
        ModelAndView mav = controladorRanking.rankingElegido(categoria);

        assertThat(mav.getViewName(), equalToIgnoringCase("ranking-categoria"));
    }

    @Test
    public void queAlElegirCategoriaElModeloContieneListaDeUsuarios() {
        String categoria = "historia";
        Set<Usuario> usuarios = new HashSet<>();
        usuarios.add(new Usuario());

        when(rankingServiceMock.buscarUsuariosPorRanking(categoria)).thenReturn(usuarios);
        ModelAndView mav = controladorRanking.rankingElegido(categoria);

        assertThat(mav.getModel().get("users").toString(), equalToIgnoringCase("usuarios encontrados"));
    }
}
