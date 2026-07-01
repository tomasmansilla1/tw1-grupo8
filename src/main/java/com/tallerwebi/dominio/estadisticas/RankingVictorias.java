package com.tallerwebi.dominio.estadisticas;

import com.tallerwebi.dominio.usuario.Usuario;

public class RankingVictorias {

    private Usuario usuario;
    private Integer partidasJugadas;
    private Integer partidasGanadas;
    private Double porcentajeVictorias;

    public RankingVictorias(Usuario usuario, Integer partidasJugadas, Integer partidasGanadas, Double porcentajeVictorias) {
        this.usuario = usuario;
        this.partidasJugadas = partidasJugadas;
        this.partidasGanadas = partidasGanadas;
        this.porcentajeVictorias = porcentajeVictorias;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Integer getPartidasJugadas() {
        return partidasJugadas;
    }

    public Integer getPartidasGanadas() {
        return partidasGanadas;
    }

    public Double getPorcentajeVictorias() {
        return porcentajeVictorias;
    }
}
