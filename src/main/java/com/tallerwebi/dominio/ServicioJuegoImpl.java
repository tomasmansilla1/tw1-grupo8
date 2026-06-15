package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.OpcionInvalidaException;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

        return repositorioJuego.buscarPreguntaPorCategoria(categoria);
    }

    @Override
    public void guardarPartida(Partida partida) {
        repositorioJuego.guardarPartida(partida);
    }

    @Override
    public Integer calcularPuntaje(List<Pregunta> listaPregunta, Respuesta respuesta) {
        Integer puntaje = 0;

        List<String> listaRespuestas = respuesta.getRespuestasUsuario();

        for (int i = 0; i < listaPregunta.size(); i++) {
            if (listaPregunta.get(i).getCorrecta().equalsIgnoreCase(listaRespuestas.get(i))) {
                puntaje+=5;
            }
        }
        return puntaje;
    }
}
