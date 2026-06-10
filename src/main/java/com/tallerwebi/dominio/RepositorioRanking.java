package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioRanking {
    List<Partida> buscarUsuariosPorRanking(String categoria);
}
