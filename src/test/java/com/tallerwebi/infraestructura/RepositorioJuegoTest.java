package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioJuego;
import com.tallerwebi.dominio.Respuesta;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.infraestructura.config.HibernateTestConfig;
import com.tallerwebi.infraestructura.config.SpringWebTestConfig;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioJuegoTest {

    private RepositorioJuego repositorioJuego;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void init() {
        this.repositorioJuego = new RepositorioJuegoImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void cuandoBuscaPreguntasPorCategoriaDeVuelveUnaLista() {
        Pregunta pregunta = new Pregunta();
        pregunta.setConsigna("¿Cuál es la capital de Francia?");
        pregunta.setCategoria("Historia");
        pregunta.setOpcionA("paris");
        pregunta.setOpcionB("monaco");
        pregunta.setOpcionC("marsella");
        pregunta.setOpcionD("lyon");
        pregunta.setCorrecta("A");

        sessionFactory.getCurrentSession().save(pregunta);

        List<Pregunta> preguntas = repositorioJuego.buscarPreguntaPorCategoria("Historia");
        assertThat(preguntas.size(), greaterThan(0));
    }

    @Test
    @Transactional
    @Rollback
    public void cuandoGuardaPartidaLaPartidaSeEnviaALaBaseDeDatos() {
        Partida partida = new Partida();
        partida.setFecha(LocalDateTime.now());
        partida.setEsVictoria(true);
        partida.setPuntajeObtenido(35);

        Respuesta respuesta = new Respuesta();
        partida.setRespuesta(respuesta);

        repositorioJuego.guardarPartida(partida);

        assertThat(partida.getId(), notNullValue());
    }

    @Test
    @Transactional
    @Rollback
    public void cuandoBuscaUnaCategoriaInexistenteDevuelveListaVacia() {

        List<Pregunta> preguntas = repositorioJuego.buscarPreguntaPorCategoria("Geografia");

        assertThat(preguntas.isEmpty(), is(true));
    }
}
