package com.tallerwebi.dominio.juego;

import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;

import java.util.List;

public interface RepositorioJuego {
    List<Pregunta> buscarPreguntaPorCategoria(String categoria);

    void guardarPartida(Partida partida);
}
