package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioEstadisticas;
import com.tallerwebi.dominio.juego.RepositorioJuego;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestConfig;
import com.tallerwebi.infraestructura.config.SpringWebTestConfig;
import com.tallerwebi.infraestructura.juego.RepositorioJuegoImpl;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioEstadisticasTest {

    private RepositorioEstadisticas repositorioEstadisticas;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void init() {
        this.repositorioEstadisticas = new RepositorioEstadisticasImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExistenPartidasVictoriosasCuandoLasBuscoObtengoSoloLasVictoriosas() {
        Partida partidaGanada1 = new Partida();
        partidaGanada1.setFecha(LocalDateTime.now());
        partidaGanada1.setPuntajeObtenido(80);
        partidaGanada1.setEsVictoria(true);

        Partida partidaGanada2 = new Partida();
        partidaGanada2.setFecha(LocalDateTime.now());
        partidaGanada2.setPuntajeObtenido(90);
        partidaGanada2.setEsVictoria(true);

        Partida partidaPerdida = new Partida();
        partidaPerdida.setFecha(LocalDateTime.now());
        partidaPerdida.setPuntajeObtenido(20);
        partidaPerdida.setEsVictoria(false);

        sessionFactory.getCurrentSession().save(partidaGanada1);
        sessionFactory.getCurrentSession().save(partidaGanada2);
        sessionFactory.getCurrentSession().save(partidaPerdida);

        List<Partida> resultado = repositorioEstadisticas.obtenerPartidasVictoriosas();

        assertThat(resultado, hasSize(2));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExistenUsuariosConRachasCuandoLosBuscoObtengoSoloLosQueTienenRachaMayorACeroOrdenadosDescendentemente() {
        Usuario usuario1 = new Usuario();
        usuario1.setUsername("Juan");
        usuario1.setRespuestasAcertadasSeguidas(5);

        Usuario usuario2 = new Usuario();
        usuario2.setUsername("Pedro");
        usuario2.setRespuestasAcertadasSeguidas(10);

        Usuario usuario3 = new Usuario();
        usuario3.setUsername("Ana");
        usuario3.setRespuestasAcertadasSeguidas(0);

        sessionFactory.getCurrentSession().save(usuario1);
        sessionFactory.getCurrentSession().save(usuario2);
        sessionFactory.getCurrentSession().save(usuario3);

        List<Usuario> resultado = repositorioEstadisticas.buscarUsuariosConMejorRachas();

        assertThat(resultado, hasSize(2));
        assertThat(resultado.get(0).getUsername(), equalTo("Pedro"));
        assertThat(resultado.get(1).getUsername(), equalTo("Juan"));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueNoExistenUsuariosConRachaCuandoLosBuscoObtengoUnaListaVacia() {

        Usuario usuario1 = new Usuario();
        usuario1.setUsername("Juan");
        usuario1.setRespuestasAcertadasSeguidas(0);

        sessionFactory.getCurrentSession().save(usuario1);

        List<Usuario> resultado = repositorioEstadisticas.buscarUsuariosConMejorRachas();

        assertThat(resultado.isEmpty(), equalTo(true));
    }
}
