package com.tallerwebi.dominio;

import com.tallerwebi.dominio.ranking.UsuariosOrdenadosDeMayorAMenorPorPuntaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@Transactional
public class RankingServiceImpl implements RankingService {

    RepositorioRanking repositorioRanking;

    @Autowired
    public RankingServiceImpl(RepositorioRanking repositorioRanking) {
        this.repositorioRanking = repositorioRanking;
    }

    @Override
    public Double calcularPuntaje(Usuario usuario, Integer puntajeBase) {
        // 1. Cada respuesta correcta suma siempre 50 puntos al puntaje base actual
        int nuevoPuntaje = puntajeBase + 50;

        // 2. Evaluamos si con esta respuesta llegó a la racha de 3 aciertos seguidos
        if (usuario.getRespuestasAcertadasSeguidas() >= 3) {
            // Le sumamos los 200 puntos extra de bonus
            nuevoPuntaje += 200;

            //  Reseteamos la racha a 0 para que vuelva a empezar
            usuario.setRespuestasAcertadasSeguidas(0);
        }

        return (double) nuevoPuntaje;
    }

    @Override
    public Set<Usuario> buscarUsuariosPorRanking(String categoria) {
        TreeSet<Usuario> listaUsuario = new TreeSet<>(new UsuariosOrdenadosDeMayorAMenorPorPuntaje());

        List<Partida> listaPreguntas = repositorioRanking.buscarUsuariosPorRanking(categoria);

        for (Partida partida : listaPreguntas) {
            if (partida.getUsuario() != null) {
                listaUsuario.add(partida.getUsuario());
            }
        }

        return listaUsuario;
    }
}