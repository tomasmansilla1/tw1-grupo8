package com.tallerwebi.dominio;

import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;

import java.util.List;

public interface ServicioJuego {
    List<Pregunta> buscarPreguntas(String categoria);

    void guardarPartida(Partida partida);

    Integer calcularPuntaje(List<Pregunta> listaPregunta, Respuesta respuesta);

    Boolean validarPartida(Integer puntaje);
}
