package com.tallerwebi.dominio.estadisticas;

import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.usuario.Usuario;

import java.util.List;
import java.util.Map;

public interface ServicioEstadisticas {

    List<Partida> buscarTodasPartidasCategorias();

    Map<String, Integer> filtrarCantidadPorCategoria(List<Partida> listaPartidas);

    List<Partida> obtenerPartidasVictoriosas();

    List<RankingTiempo> usuariosConMejorTiempo(List<Partida> listaPartida, String ordenamiento);

    List<Usuario> usuariosConMejorRacha();
}
