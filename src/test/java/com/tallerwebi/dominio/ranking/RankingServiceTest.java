package com.tallerwebi.dominio.ranking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.Test;

public class RankingServiceTest {

  @Test
  public void siElUsuarioTieneUnaRachaDeUnAciertoDebeSumarSoloCincuentaPuntos() {

    RankingService rankingService = new RankingServiceImpl();

    Usuario usuario = new Usuario();
    usuario.setPartidasGanadasSeguidas(1);

    Integer resultado = rankingService.calcularPuntaje(usuario, 0);

    assertEquals(50, resultado);
    assertEquals(1, usuario.getPartidasGanadasSeguidas());
  }

  @Test
  public void siElUsuarioAlcanzaTresAciertosSeguidosDebeRecibirBonusDeDoscientosPuntos() {

    RankingService rankingService = new RankingServiceImpl();

    Usuario usuario = new Usuario();
    usuario.setPartidasGanadasSeguidas(3);

    Integer resultado = rankingService.calcularPuntaje(usuario, 0);

    assertEquals(250, resultado);
    assertEquals(0, usuario.getPartidasGanadasSeguidas());
  }

  @Test
  public void debeSumarAlPuntajeExistente() {

    RankingService rankingService = new RankingServiceImpl();

    Usuario usuario = new Usuario();
    usuario.setPartidasGanadasSeguidas(1);

    Integer resultado = rankingService.calcularPuntaje(usuario, 100);

    assertEquals(150, resultado);
  }

  @Test
  public void debeAgregarBonusAlPuntajeExistente() {

    RankingService rankingService = new RankingServiceImpl();

    Usuario usuario = new Usuario();
    usuario.setPartidasGanadasSeguidas(3);

    Integer resultado = rankingService.calcularPuntaje(usuario, 100);

    assertEquals(350, resultado);
  }

  @Test
  public void noDebeResetearLaRachaSiTodaviaNoLlegoATres() {

    RankingService rankingService = new RankingServiceImpl();

    Usuario usuario = new Usuario();
    usuario.setPartidasGanadasSeguidas(2);

    rankingService.calcularPuntaje(usuario, 0);
  }
}
