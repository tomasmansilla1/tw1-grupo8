package com.tallerwebi.dominio;

public interface RankingService {
    // pide recibir un usuario y el puntaje base del juego actual
    Double calcularPuntaje(Usuario usuario, Integer puntajeBase);
}