package com.tallerwebi.presentacion.estadistica;

import com.tallerwebi.dominio.estadisticas.RankingTiempo;
import com.tallerwebi.dominio.estadisticas.RankingVictorias;
import com.tallerwebi.dominio.estadisticas.ServicioEstadisticas;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.estadisticas.ControladorEstadisticas;
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

        ModelAndView nav = controladorEstadisticas.esatdisticasTiempo("");

        assertThat(nav.getViewName(), equalToIgnoringCase("tiempo"));
        assertThat(nav.getModel().get("listaVacio"), nullValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void dadoQueElUsuarioEntreALasEstadisticasDeTiempoPodraVerUnRankingConLosMenoresTiemposEnPartida() {
        List<Partida> listaPartidas = new ArrayList<>();
        Partida partida = new Partida();

        List<RankingTiempo> rankingTiempos = new ArrayList<>();
        RankingTiempo rankingTiempo = new RankingTiempo(new Usuario(), 90L);

        listaPartidas.add(partida);
        rankingTiempos.add(rankingTiempo);

        when(servicioEstadisticas.obtenerPartidasVictoriosas()).thenReturn(listaPartidas);
        when(servicioEstadisticas.usuariosConMejorTiempo(listaPartidas, "")).thenReturn(rankingTiempos);

        ModelAndView nav = controladorEstadisticas.esatdisticasTiempo("");

        List<RankingTiempo> resultado = (List<RankingTiempo>) nav.getModel().get("usuariosTiempo");

        assertThat(nav.getViewName(), equalToIgnoringCase("tiempo"));
        assertThat(resultado, hasSize(1));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void dadoQueExistenUsuariosConPartidasTantoVictoriosasYPerdidasObtendreSuPorcentajeMasSuTotalDePartidas() {
        Usuario usuario1 = new Usuario();
        usuario1.setUsername("Mauricio");

        Usuario usuario2 = new Usuario();
        usuario2.setUsername("Juan");

        List<RankingVictorias> ranking = new ArrayList<>();
        ranking.add(new RankingVictorias(usuario1, 10, 8, 80.0));
        ranking.add(new RankingVictorias(usuario2, 20, 15, 75.0));

        when(servicioEstadisticas.usuariosConMejorPartida()).thenReturn(ranking);

        ModelAndView nav = controladorEstadisticas.estadisticasPorcentajeVictorias();

        assertThat(nav.getViewName(), equalToIgnoringCase("porcentaje-victorias"));

        List<RankingVictorias> resultado = (List<RankingVictorias>) nav.getModel().get("rankingVictorias");

        assertThat(resultado.size(), equalTo(2));
        assertThat(resultado.get(0).getUsuario().getUsername(), equalToIgnoringCase("Mauricio"));
        assertThat(resultado.get(0).getPorcentajeVictorias(), equalTo(80.0));
    }
}
