package com.tallerwebi.dominio;


import java.util.Set;

public interface RankingService {
    // pide recibir un usuario y el puntaje base del juego actual
    Double calcularPuntaje(Usuario usuario, Integer puntajeBase);
    Set<Usuario> buscarUsuariosPorRanking(String categoria);

}