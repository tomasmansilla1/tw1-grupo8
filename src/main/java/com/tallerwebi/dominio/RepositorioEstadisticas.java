package com.tallerwebi.dominio;

import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.usuario.Usuario;

import java.util.List;

public interface RepositorioEstadisticas {
    List<Partida> buscarPartidasFinalizadas();

    List<Partida> obtenerPartidasVictoriosas();

    List<Usuario> buscarUsuariosConMejorRachas();
}
