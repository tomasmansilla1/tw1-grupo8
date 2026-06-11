package com.tallerwebi.dominio.ranking;

import com.tallerwebi.dominio.usuario.Usuario;

public class RankingServiceImpl implements RankingService {

  private static final int RESPUESTAS_PARA_BONUS = 3;
  private static final int PUNTOS_POR_RESPUESTA = 50;
  private static final int BONUS_RACHA = 200;

  @Override
  public Double calcularPuntaje(Usuario usuario, Integer puntajeBase) {
    int nuevoPuntaje = puntajeBase + PUNTOS_POR_RESPUESTA;

    if (usuario.getRespuestasAcertadasSeguidas() >= RESPUESTAS_PARA_BONUS) {
      nuevoPuntaje += BONUS_RACHA;
    }
    return (double) nuevoPuntaje;
  }
}
