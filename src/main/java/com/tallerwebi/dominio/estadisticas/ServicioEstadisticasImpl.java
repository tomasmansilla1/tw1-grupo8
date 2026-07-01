package com.tallerwebi.dominio.estadisticas;

import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class ServicioEstadisticasImpl implements ServicioEstadisticas {

    RepositorioEstadisticas repositorioEstadisticas;

    @Autowired
    public ServicioEstadisticasImpl(RepositorioEstadisticas repositorioEstadisticas) {
        this.repositorioEstadisticas = repositorioEstadisticas;
    }

    @Override
    public List<Partida> buscarTodasPartidasCategorias() {
        List<Partida> buscarPartidasPorCategoria = repositorioEstadisticas.buscarPartidasFinalizadas();

        if (buscarPartidasPorCategoria.isEmpty()) {
            return null;
        }

        return buscarPartidasPorCategoria;
    }

    @Override
    public Map<String, Integer> filtrarCantidadPorCategoria(List<Partida> listaPartidas) {
        if (listaPartidas.isEmpty()) {
            return null;
        }

        Map<String, Integer> cantidadPorCategoria = new HashMap<>();

        for (Partida partida : listaPartidas) {
            String categoria = partida.getCategoria();

            if (!cantidadPorCategoria.containsKey(categoria)) {
                cantidadPorCategoria.put(categoria, 1);
            } else {
                Integer cantidadActual = cantidadPorCategoria.get(categoria);
                cantidadPorCategoria.put(categoria, cantidadActual + 1);
            }
        }
        return cantidadPorCategoria;
    }

    @Override
    public List<Partida> obtenerPartidasVictoriosas() {
        return repositorioEstadisticas.obtenerPartidasVictoriosas();
    }

    @Override
    public List<RankingTiempo> usuariosConMejorTiempo(List<Partida> listaPartida, String ordenamiento) {
        List<RankingTiempo> rankingTiempos = new ArrayList<>();
        Map<Usuario, Long> mejoresTiempo = obtenerTiempoUsuario(listaPartida);

        for (Map.Entry<Usuario, Long> entry : mejoresTiempo.entrySet()) {
            rankingTiempos.add(new RankingTiempo(entry.getKey(), entry.getValue()));
        }

        if (ordenamiento == null || ordenamiento.isEmpty() || ordenamiento.equalsIgnoreCase("ASC")) {
            rankingTiempos.sort(Comparator.comparing(RankingTiempo::getTiempo));
        }

        if ("DESC".equalsIgnoreCase(ordenamiento)) {
            rankingTiempos.sort(Comparator.comparing(RankingTiempo::getTiempo).reversed());
        }

        return rankingTiempos;
    }


    private Map<Usuario, Long> obtenerTiempoUsuario(List<Partida> listaPartida) {
        Map<Usuario, Long> mejoresTiempo = new HashMap<>();

        for (Partida partida : listaPartida) {
            Usuario usuario = partida.getUsuario();

            long tiempo = ChronoUnit.SECONDS.between(
                    partida.getInicioPartida(),
                    partida.getFinalPartida());

            if (!mejoresTiempo.containsKey(usuario)) {
                mejoresTiempo.put(usuario, tiempo);
            } else {
                if (tiempo < mejoresTiempo.get(usuario)) {
                    mejoresTiempo.put(usuario, tiempo);
                }
            }
        }

        return mejoresTiempo;
    }


    @Override
    public List<RankingVictorias> usuariosConMejorPartida() {
        List<Partida> partidas = repositorioEstadisticas.buscarPartidasFinalizadas();

        if (partidas.isEmpty()) {
            return null;
        }

        return calcularPorcentajeVictorias(partidas);
    }

    private List<RankingVictorias> calcularPorcentajeVictorias(List<Partida> partidas) {
        Map<Usuario, Integer> jugadas = new HashMap<>();
        Map<Usuario, Integer> ganadas = new HashMap<>();

        for (Partida partida : partidas) {
            Usuario usuario = partida.getUsuario();

            jugadas.put(usuario, jugadas.getOrDefault(usuario, 0) + 1);

            if (partida.getEsVictoria()) {
                ganadas.put(usuario, ganadas.getOrDefault(usuario, 0) + 1);
            }
        }

        List<RankingVictorias> ranking = new ArrayList<>();

        for (Usuario usuario : jugadas.keySet()) {
            int totalJugadas = jugadas.get(usuario);
            int totalGanadas = ganadas.getOrDefault(usuario, 0);

            double porcentaje = ((double) totalGanadas * 100) / totalJugadas;

            ranking.add(new RankingVictorias(usuario, totalJugadas, totalGanadas, porcentaje));
        }

        ranking.sort(Comparator.comparing(RankingVictorias::getPorcentajeVictorias).reversed());

        return ranking;
    }
}
