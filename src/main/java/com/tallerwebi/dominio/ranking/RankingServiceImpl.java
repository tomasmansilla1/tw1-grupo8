package com.tallerwebi.dominio.ranking;

import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RankingServiceImpl implements RankingService {

  private RepositoryUsuario repositorioUsuario;

  private static final int RESPUESTAS_PARA_BONUS = 3;
  private static final int PUNTOS_POR_RESPUESTA = 50;
  private static final int BONUS_RACHA = 200;

  @Autowired
  public RankingServiceImpl(RepositoryUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public Double calcularPuntaje(Usuario usuario, Integer puntajeBase) {
    int nuevoPuntaje = puntajeBase + PUNTOS_POR_RESPUESTA;

    System.out.println("DEBUG: Racha antes del IF: " + usuario.getRespuestasAcertadasSeguidas());
    System.out.println("DEBUG: Constante RESPUESTAS_PARA_BONUS: " + RESPUESTAS_PARA_BONUS);

    if (usuario.getRespuestasAcertadasSeguidas() >= RESPUESTAS_PARA_BONUS) {
      nuevoPuntaje += BONUS_RACHA;
      usuario.setRespuestasAcertadasSeguidas(0);
      System.out.println("DEBUG: Entró al IF, racha reseteada a: " + usuario.getRespuestasAcertadasSeguidas());
    }

    return (double) nuevoPuntaje;
  }

  @Override
  public List<Usuario> obtenerTop10() {
    return repositorioUsuario.obtenerTopUsuarios();
  }

}