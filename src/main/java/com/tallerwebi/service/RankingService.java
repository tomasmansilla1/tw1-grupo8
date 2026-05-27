package com.tallerwebi.service;

import com.tallerwebi.model.Usuario;

public interface RankingService {
    // pide recibir un usuario y el puntaje base del juego actual
    Double calcularPuntaje(Usuario usuario, Integer puntajeBase);
}