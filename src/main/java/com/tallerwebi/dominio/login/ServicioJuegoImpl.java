package com.tallerwebi.dominio.login;

import com.tallerwebi.dominio.excepcion.OpcionInvalidaException;
import com.tallerwebi.dominio.juego.RepositorioJuego;
import com.tallerwebi.dominio.juego.Respuesta;
import com.tallerwebi.dominio.juego.ServicioJuego;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {

    RepositorioJuego repositorioJuego;

    @Autowired
    public ServicioJuegoImpl(RepositorioJuego repositorioJuego) {
        this.repositorioJuego = repositorioJuego;
    }

    @Override
    public List<Pregunta> buscarPreguntas(String categoria) {

        if (categoria.isEmpty()) {
            throw new OpcionInvalidaException("debe elegir una opcion");
        }

        List<Pregunta> preguntas = repositorioJuego.buscarPreguntaPorCategoria(categoria);

        Collections.shuffle(preguntas);

        return preguntas;
    }

    @Override
    public void guardarPartida(Partida partida) {
        repositorioJuego.guardarPartida(partida);
    }

    @Override
    public Integer calcularPuntaje(List<Pregunta> listaPregunta, Respuesta respuesta) {
        Integer puntaje = 0;

        List<String> listaRespuestas = respuesta.getRespuestasUsuario();
        int limite = Math.min(listaPregunta.size(), listaRespuestas.size());

        for (int i = 0; i < limite; i++) {
            if (listaPregunta.get(i).getCorrecta().equalsIgnoreCase(listaRespuestas.get(i))) {
                puntaje+=5;
            }
        }
        return puntaje;
    }

    @Override
    public Boolean validarPartida(Integer puntaje) {
        if (puntaje >= 35) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer calcularRespuestasCorrectas(List<Pregunta> preguntas, Respuesta respuesta) {
        Integer correctas = 0;

        for (int i = 0; i < preguntas.size(); i++) {

        if (preguntas.get(i).getCorrecta().equalsIgnoreCase(
            respuesta.getRespuestasUsuario().get(i)
        )) {
            correctas++;
        }
        }
        return correctas;
    }

    @Override
    public List<Partida> buscarHistorial(Long usuarioId) {
        return repositorioJuego.buscarPartidasPorUsuario(usuarioId);
    }
}
