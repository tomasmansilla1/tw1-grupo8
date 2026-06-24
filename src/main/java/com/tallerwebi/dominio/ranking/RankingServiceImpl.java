package com.tallerwebi.dominio.ranking;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.usuario.Usuario;

@Service
public class RankingServiceImpl implements RankingService {

  private static final int RESPUESTAS_PARA_BONUS = 3;
  private static final int BONUS_RACHA = 200;

  @Override
  public Integer calcularPuntaje(Usuario usuario,Integer puntajeBase) {

    int nuevoPuntaje = puntajeBase;

    if (usuario.getPartidasGanadasSeguidas() >= RESPUESTAS_PARA_BONUS) {
      nuevoPuntaje += BONUS_RACHA;
    }

    return nuevoPuntaje;
  }
}
