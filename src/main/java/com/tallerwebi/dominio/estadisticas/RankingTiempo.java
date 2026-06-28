package com.tallerwebi.dominio.estadisticas;

import com.tallerwebi.dominio.usuario.Usuario;

public class RankingTiempo {

    private Usuario usuario;
    private Long tiempo;

    public RankingTiempo(Usuario usuario, Long tiempo) {
        this.usuario = usuario;
        this.tiempo = tiempo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTiempoFormateado() {
        long minutos = tiempo / 60;
        long segundos = tiempo % 60;

        return String.format("%02d:%02d", minutos, segundos);
    }

    public void setTiempo(Long tiempo) {
        this.tiempo = tiempo;
    }

    public Long getTiempo() {
        return tiempo;
    }
}
