package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioJuego;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.infraestructura.config.HibernateTestConfig;
import com.tallerwebi.infraestructura.config.SpringWebTestConfig;

import org.hibernate.Session;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.hibernate.query.Query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioJuegoTest {

    private RepositorioJuego repositorioJuego;
    private Session session;
    private Query<Partida> query;

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void init() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        query = mock(Query.class);
        this.repositorioJuego = new RepositorioJuegoImpl(sessionFactory);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
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
    void debeBuscarPartidasPorUsuario() {

        Long usuarioId = 1L;

        Partida partida1 = new Partida();
        Partida partida2 = new Partida();

        List<Partida> partidas = new ArrayList<>();
        partidas.add(partida1);
        partidas.add(partida2);

        when(session.createQuery(
                "FROM Partida p WHERE p.usuario.id = :id ORDER BY p.fecha DESC",
                Partida.class))
            .thenReturn(query);

        when(query.setParameter("id", usuarioId))
            .thenReturn(query);

        when(query.list())
            .thenReturn(partidas);

        List<Partida> resultado = repositorioJuego.buscarPartidasPorUsuario(usuarioId);

        assertEquals(2, resultado.size());

        verify(query).setParameter("id", usuarioId);
        verify(query).list();
    }
}
