package com.tallerwebi.dominio.ranking;

import java.util.List;
import com.tallerwebi.dominio.usuario.Usuario;

public interface RankingService {
  // pide recibir un usuario y el puntaje base del juego actual
  Double calcularPuntaje(Usuario usuario, Integer puntajeBase);

  List<Usuario> obtenerTop10();
}