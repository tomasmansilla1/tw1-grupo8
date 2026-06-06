package com.tallerwebi.infraestructura.pregunta;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.pregunta.Pregunta;


@Transactional
public class RepositoryPreguntasImplTest {

    private SessionFactory sessionFactory;
    private Session session;

    private RepositoryPreguntasImpl repository;

    @BeforeEach
    public void init() {

        // mocks manuales
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);

        repository = new RepositoryPreguntasImpl();
        repository.sessionFactory = sessionFactory;

        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void debeGuardarPregunta() {
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta("Pregunta test");

        repository.save(pregunta);

        verify(session).saveOrUpdate(pregunta);
    }

    @Test
    public void debeEliminarPreguntaSiExiste() {

        Pregunta pregunta = new Pregunta();
        pregunta.setId(1L);

        when(session.get(Pregunta.class,1L)).thenReturn(pregunta);

        repository.deleteById(1L);
        verify(session).delete(pregunta);
    }

    @Test
    public void noDebeEliminarPreguntaSiNoExiste() {
        when(session.get(Pregunta.class,1L)).thenReturn(null);

        repository.deleteById(1L);
        verify(session, never()).delete(any());
    }
}