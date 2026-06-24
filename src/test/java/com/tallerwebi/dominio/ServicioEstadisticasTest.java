package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ListaUsuariosVaciaException;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioEstadisticasTest {

    ServicioEstadisticas servicioEstadisticas;
    RepositorioEstadisticas repositorioEstadisticas;

    @BeforeEach
    public void init() {
        repositorioEstadisticas = mock(RepositorioEstadisticas.class);
        servicioEstadisticas = new ServicioEstadisticasImpl(repositorioEstadisticas);
    }

    @Test
    public void cuandoSeFiltranLasPartidasPorCategoriaSeObtieneLaCantidadCorrectaPorCadaUna() {
        List<Partida> partidas = new ArrayList<>();

        Partida partida1 = new Partida();
        partida1.setCategoria("Historia");

        Partida partida2 = new Partida();
        partida2.setCategoria("Historia");

        Partida partida3 = new Partida();
        partida3.setCategoria("Deporte");

        partidas.add(partida1);
        partidas.add(partida2);
        partidas.add(partida3);

        Map<String, Integer> resultado = servicioEstadisticas.filtrarCantidadPorCategoria(partidas);

        assertThat(resultado.get("Historia"), equalTo(2));
        assertThat(resultado.get("Deporte"), equalTo(1));
        assertThat(resultado.size(), equalTo(2));
    }

    @Test
    public void dadoQueCuandoElUsuarioIngreseALasEstadisticasDeTiempoVaAVerUnRankingYElPrimeroVaASerElDeMenorTiempo() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("Mauricio");

        Usuario usuario2 = new Usuario();
        usuario1.setId(2L);
        usuario2.setUsername("Carlos");

        Usuario usuario3 = new Usuario();
        usuario1.setId(3L);
        usuario3.setUsername("Juan");

        Partida partida1 = new Partida();
        partida1.setUsuario(usuario1);
        partida1.setInicioPartida(LocalTime.of(15, 35, 00));
        partida1.setFinalPartida(LocalTime.of(15, 37, 35));

        Partida partida2 = new Partida();
        partida2.setUsuario(usuario2);
        partida2.setInicioPartida(LocalTime.of(10, 10, 30));
        partida2.setFinalPartida(LocalTime.of(10, 12, 00));

        Partida partida3 = new Partida();
        partida3.setUsuario(usuario3);
        partida3.setInicioPartida(LocalTime.of(20, 35, 00));
        partida3.setFinalPartida(LocalTime.of(20, 37, 10));

        List<Partida> partidas = new ArrayList<>();
        partidas.add(partida1);
        partidas.add(partida2);
        partidas.add(partida3);

        List<RankingTiempo> ranking = servicioEstadisticas.usuariosConMejorTiempo(partidas);

        assertThat(ranking, hasSize(3));
        assertThat(ranking.get(0).getUsuario(), equalTo(usuario2));
        assertThat(ranking.get(0).getTiempo(), equalTo(90L));
    }

    @Test
    public void cuandoExistenUsuariosConRachaSeDevuelveLaLista() {
        List<Usuario> usuarios = new ArrayList<>();

        Usuario usuario = new Usuario();
        usuario.setUsername("Mauricio");

        usuarios.add(usuario);

        when(repositorioEstadisticas.buscarUsuariosConMejorRachas()).thenReturn(usuarios);

        List<Usuario> resultado = servicioEstadisticas.usuariosConMejorRacha();

        assertThat(resultado, hasSize(1));
        assertThat(resultado.get(0).getUsername(), equalTo("Mauricio"));
    }

    @Test
    public void cuandoNoExistenUsuariosConRachaSeLanzaUnaExcepcion() {
        when(repositorioEstadisticas.buscarUsuariosConMejorRachas()).thenReturn(null);

        assertThrows(ListaUsuariosVaciaException.class,() -> servicioEstadisticas.usuariosConMejorRacha());
    }
}
