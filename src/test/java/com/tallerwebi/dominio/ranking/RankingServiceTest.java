package com.tallerwebi.dominio.ranking;

import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RankingServiceTest {

  private RankingService rankingService;
  private RepositoryUsuario repositoryUsuarioMock;

  @BeforeEach
  public void init() {
    // Mockeamos el repositorio para los tests de ranking
    repositoryUsuarioMock = mock(RepositoryUsuario.class);
    // Instanciamos el servicio con el mock
    rankingService = new RankingServiceImpl();
  }

  // --- TESTS DE LÓGICA DE PUNTOS ---

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

  // --- TEST DE INTEGRACIÓN DE RANKING ---

  @Test
  public void queSePuedaObtenerElTop10DeUsuariosUsandoElRepositorio() {
    // Preparación
    List<Usuario> usuariosTop = new ArrayList<>();
    usuariosTop.add(new Usuario());

    when(repositoryUsuarioMock.obtenerTopUsuarios()).thenReturn(usuariosTop);

    // Ejecución
    List<Usuario> resultado = rankingService.obtenerTop10();

    // Validación
    assertThat(resultado, equalTo(usuariosTop));
    verify(repositoryUsuarioMock, times(1)).obtenerTopUsuarios();
  }

  @Test
  public void siElUsuarioLlegaALaTerceraRespuestaSeguidaDebeSumarCincuentaPuntosMasDoscientosDeBonusYResetearRacha() {
    Usuario usuario = new Usuario();
    usuario.setPartidasGanadasSeguidas(3); // Ya tiene 3
    Integer puntajeBase = 0;

    Integer resultado = rankingService.calcularPuntaje(usuario, puntajeBase);

    assertEquals(250, resultado); // 0 + 50 + 200
    assertEquals(0, usuario.getPartidasGanadasSeguidas()); // Se resetea
  }
}