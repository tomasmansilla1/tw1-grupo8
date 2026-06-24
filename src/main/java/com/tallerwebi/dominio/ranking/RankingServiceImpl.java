package com.tallerwebi.dominio.ranking;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;

@Service
public class RankingServiceImpl implements RankingService {

  private static final int RESPUESTAS_PARA_BONUS = 3;
  private static final int BONUS_RACHA = 200;

  RepositoryUsuario repositoryUsuario;

  @Override
  public Integer calcularPuntaje(Usuario usuario,Integer puntajeBase) {

    int nuevoPuntaje = puntajeBase;

    if (usuario.getPartidasGanadasSeguidas() >= RESPUESTAS_PARA_BONUS) {
      nuevoPuntaje += BONUS_RACHA;
      usuario.setPartidasGanadasSeguidas(puntajeBase);
      System.out.println("DEBUG: Entró al IF, racha reseteada a: " + usuario.getPartidasGanadasSeguidas());
    }

    return nuevoPuntaje;
  }

  @Override
  public List<Usuario> obtenerTop10() {
    return repositoryUsuario.obtenerTopUsuarios();
  }

}