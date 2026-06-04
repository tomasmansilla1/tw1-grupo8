package com.tallerwebi.dominio;

public class RankingServiceImpl implements RankingService {

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
}