package com.tallerwebi.dominio.estadisticas;

import com.tallerwebi.dominio.partida.Partida;
import java.util.List;

public interface RepositorioEstadisticas {
    List<Partida> buscarPartidasFinalizadas();

    List<Partida> obtenerPartidasVictoriosas();

}
