package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ListaUsuariosVaciaException;
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
        return repositorioEstadisticas.buscarPartidasFinalizadas();
    }

    @Override
    public Map<String, Integer> filtrarCantidadPorCategoria(List<Partida> listaPartidas) {
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
    public List<RankingTiempo> usuariosConMejorTiempo(List<Partida> listaPartida) {
        List<RankingTiempo> rankingTiempos = new ArrayList<>();
        Map<Usuario, Long> mejoresTiempo = obtenerTiempoUsuario(listaPartida);

        for (Map.Entry<Usuario, Long> entry : mejoresTiempo.entrySet()) {
            rankingTiempos.add(new RankingTiempo(entry.getKey(), entry.getValue()));
        }

        rankingTiempos.sort(Comparator.comparing(RankingTiempo::getTiempo));

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
    public List<Usuario> usuariosConMejorRacha() {
        List<Usuario> listaUsuarios = repositorioEstadisticas.buscarUsuariosConMejorRachas();

        if (listaUsuarios == null) {
            throw new ListaUsuariosVaciaException("no hay usuarios con rachas cargadas");
        }

        return listaUsuarios;
    }
}
