package com.tallerwebi.dominio.ranking;

import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.usuario.Usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RankingServiceTest {

    @Test
    public void siElUsuarioAciertaUnaPreguntaNormalDebeSumarCincuentaPuntosYNoDarBonus() {
        // Preparación (Usuario con racha de 1 acierto, puntaje inicial de 0)
        RankingService rankingService = new RankingServiceImpl();
        Usuario usuario = new Usuario();
        usuario.setRespuestasAcertadasSeguidas(1);
        Integer puntajeBase = 0;

        // Ejecución
        Double resultado = rankingService.calcularPuntaje(usuario, puntajeBase);

        // Verificación: 0 + 50 = 50.0. La racha de aciertos se debe mantener en 1
        assertEquals(50.0, resultado);
        assertEquals(1, usuario.getRespuestasAcertadasSeguidas());
    }

    @Test
    public void siElUsuarioLlegaALaTerceraRespuestaSeguidaDebeSumarCincuentaPuntosMasDoscientosDeBonusYResetearRacha() {
        // Preparación (Usuario que ya tenía 2 aciertos y mete el 3, puntaje inicial de 0)
        RankingService rankingService = new RankingServiceImpl();
        Usuario usuario = new Usuario();
        usuario.setRespuestasAcertadasSeguidas(3);
        Integer puntajeBase = 0;

        // Ejecución
        Double resultado = rankingService.calcularPuntaje(usuario, puntajeBase);

        // Verificación: 0 + 50 (normal) + 200 (bonus) = 250.0
        assertEquals(250.0, resultado);

        // Verificación de negocio: El contador de rachas debe haber vuelto a 0
        assertEquals(0, usuario.getRespuestasAcertadasSeguidas());
    }
}