package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RankingServiceTest {

    RankingServiceImpl rankingService;
    RepositorioRanking repositorioRankingMock;

    @BeforeEach
    public void init() {
        repositorioRankingMock = mock(RepositorioRanking.class);
        rankingService = new RankingServiceImpl(repositorioRankingMock);
    }

    @Test
    public void siElUsuarioAciertaUnaPreguntaNormalDebeSumarCincuentaPuntosYNoDarBonus() {
        // Preparación (Usuario con racha de 1 acierto, puntaje inicial de 0)
        RankingService rankingService = new RankingServiceImpl(repositorioRankingMock);
        Usuario usuario = new Usuario();
        usuario.setRespuestasAcertadasSeguidas(1);
        Integer puntajeBase = 0;

        // Ejecución
        Double resultado = rankingService.calcularPuntaje(usuario, puntajeBase);

        // Verificación: 0 + 50 = 50.0. La racha de aciertos se debe mantener en 1
        assertEquals(50.0, resultado);
        assertEquals(1, usuario.getRespuestasAcertadasSeguidas());
    }

    @Test
    public void siElUsuarioLlegaALaTerceraRespuestaSeguidaDebeSumarCincuentaPuntosMasDoscientosDeBonusYResetearRacha() {
        // Preparación (Usuario que ya tenía 2 aciertos y mete el 3, puntaje inicial de 0)
        RankingService rankingService = new RankingServiceImpl(repositorioRankingMock);
        Usuario usuario = new Usuario();
        usuario.setRespuestasAcertadasSeguidas(3);
        Integer puntajeBase = 0;

        // Ejecución
        Double resultado = rankingService.calcularPuntaje(usuario, puntajeBase);

        // Verificación: 0 + 50 (normal) + 200 (bonus) = 250.0
        assertEquals(250.0, resultado);

        // Verificación de negocio: El contador de rachas debe haber vuelto a 0
        assertEquals(0, usuario.getRespuestasAcertadasSeguidas());
    }

    @Test
    public void queBuscarUsuariosPorRankingRetornaUsuariosOrdenados() {
        String categoria = "historia";

        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setPuntaje(100);

        Usuario usuario2 = new Usuario();
        usuario1.setId(2L);
        usuario2.setPuntaje(200);

        Partida partida1 = new Partida();
        partida1.setUsuario(usuario1);

        Partida partida2 = new Partida();
        partida2.setUsuario(usuario2);

        List<Partida> partidas = new ArrayList<>();
        partidas.add(partida1);
        partidas.add(partida2);

        when(repositorioRankingMock.buscarUsuariosPorRanking(categoria))
                .thenReturn(partidas);

        Set<Usuario> resultado = rankingService.buscarUsuariosPorRanking(categoria);

        List<Usuario> listaOrdenada = new ArrayList<>(resultado);

        assertThat(listaOrdenada.get(0).getPuntaje(), equalTo(200));
        assertThat(listaOrdenada.get(1).getPuntaje(), equalTo(100));
    }
}