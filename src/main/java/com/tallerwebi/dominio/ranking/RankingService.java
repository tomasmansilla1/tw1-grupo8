package com.tallerwebi.dominio.ranking;

import java.util.List;
import com.tallerwebi.dominio.usuario.Usuario;

public interface RankingService {
  // pide recibir un usuario y el puntaje base del juego actual
  Integer calcularPuntaje(Usuario usuario, Integer puntajeBase);

  List<Usuario> obtenerTop10();
}
