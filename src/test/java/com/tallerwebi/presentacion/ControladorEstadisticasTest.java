package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RankingTiempo;
import com.tallerwebi.dominio.ServicioEstadisticas;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorEstadisticasTest {

    private ControladorEstadisticas controladorEstadisticas;
    private ServicioEstadisticas servicioEstadisticas;

    @BeforeEach
    public void init() {
        this.servicioEstadisticas = mock(ServicioEstadisticas.class);
        this.controladorEstadisticas = new ControladorEstadisticas(servicioEstadisticas);
    }

    @Test
    public void dadoQueElUsuarioIngreseALaSeccionDeEstadisticasVaAMostrarLasDeCategoriasMasElegidas() {
        List<Partida> listaPartidas = new ArrayList<>();
        Map<String, Integer> categoriaYCantidad = new HashMap<>();
        categoriaYCantidad.put("Historia", 2);

        Partida partida1 = new Partida();
        listaPartidas.add(partida1);

        when(servicioEstadisticas.buscarTodasPartidasCategorias()).thenReturn(listaPartidas);
        when(servicioEstadisticas.filtrarCantidadPorCategoria(listaPartidas)).thenReturn(categoriaYCantidad);

        ModelAndView nav = controladorEstadisticas.estadisticasCategoria();

        assertThat(nav.getViewName(), equalToIgnoringCase("categoria"));
        assertThat(nav.getModel().get("listaCategorias"), equalTo(categoriaYCantidad));
    }


    @Test
    public void dadoQueElUsuarioQuieraVerLasEstadisticasDelMenorTiempoYNoHayaPartidasRegistradasVaAVerUnMensajeDeError() {
        List<Partida> listaPartidas = new ArrayList<>();

        when(servicioEstadisticas.obtenerPartidasVictoriosas()).thenReturn(listaPartidas);

        ModelAndView nav = controladorEstadisticas.esatdisticasTiempo();

        assertThat(nav.getViewName(), equalToIgnoringCase("tiempo"));
        assertThat(nav.getModel().get("listaVacio"), is(listaPartidas));
    }

    @Test
    public void dadoQueElUsuarioEntreALasEstadisticasDeTiempoPodraVerUnRankingConLosMenoresTiemposEnPartida() {
        List<Partida> listaPartidas = new ArrayList<>();
        Partida partida = new Partida();

        List<RankingTiempo> rankingTiempos = new ArrayList<>();
        RankingTiempo rankingTiempo = new RankingTiempo(new Usuario(), 90L);

        listaPartidas.add(partida);
        rankingTiempos.add(rankingTiempo);

        when(servicioEstadisticas.obtenerPartidasVictoriosas()).thenReturn(listaPartidas);
        when(servicioEstadisticas.usuariosConMejorTiempo(listaPartidas)).thenReturn(rankingTiempos);

        ModelAndView nav = controladorEstadisticas.esatdisticasTiempo();

        List<RankingTiempo> resultado = (List<RankingTiempo>) nav.getModel().get("usuariosTiempo");

        assertThat(nav.getViewName(), equalToIgnoringCase("tiempo"));
        assertThat(resultado, hasSize(1));
    }

    @Test
    public void dadoQueExistenUsuariosConRachaCuandoSeConsultanLasEstadisticasSeObtieneElRanking() {
        List<Usuario> usuarios = new ArrayList<>();

        Usuario usuario = new Usuario();
        usuario.setUsername("Mauricio");
        usuario.setRespuestasAcertadasSeguidas(15);

        usuarios.add(usuario);

        when(servicioEstadisticas.usuariosConMejorRacha()).thenReturn(usuarios);

        ModelAndView nav = controladorEstadisticas.estadisticasRacha();

        List<Usuario> resultado = (List<Usuario>) nav.getModel().get("usuariosRacha");

        assertThat(nav.getViewName(), equalToIgnoringCase("racha"));
        assertThat(resultado, hasSize(1));
        assertThat(resultado.get(0).getUsername(), equalTo("Mauricio"));
    }
}
