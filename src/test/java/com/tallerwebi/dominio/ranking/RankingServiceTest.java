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
    rankingService = new RankingServiceImpl(repositoryUsuarioMock);
  }

  // --- TESTS DE LÓGICA DE PUNTOS ---

  @Test
  public void siElUsuarioAciertaUnaPreguntaNormalDebeSumarCincuentaPuntosYNoDarBonus() {
    Usuario usuario = new Usuario();
    usuario.setRespuestasAcertadasSeguidas(1);
    Integer puntajeBase = 0;

    Double resultado = rankingService.calcularPuntaje(usuario, puntajeBase);

    assertEquals(50.0, resultado);
    assertEquals(1, usuario.getRespuestasAcertadasSeguidas()); // La racha se mantiene
  }

  @Test
  public void siElUsuarioLlegaALaTerceraRespuestaSeguidaDebeSumarCincuentaPuntosMasDoscientosDeBonusYResetearRacha() {
    Usuario usuario = new Usuario();
    usuario.setRespuestasAcertadasSeguidas(3); // Ya tiene 3
    Integer puntajeBase = 0;

    Double resultado = rankingService.calcularPuntaje(usuario, puntajeBase);

    assertEquals(250.0, resultado); // 0 + 50 + 200
    assertEquals(0, usuario.getRespuestasAcertadasSeguidas()); // Se resetea
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
}