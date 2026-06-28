package com.tallerwebi.infraestructura.pregunta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tallerwebi.dominio.pregunta.Pregunta;


@Transactional
public class RepositoryPreguntasImplTest {

    private SessionFactory sessionFactory;
    private Session session;

    @Mock
    private Query<Pregunta> query;

    private RepositoryPreguntasImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void init() {
        // mocks manuales
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);

        repository = new RepositoryPreguntasImpl(sessionFactory);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void debeGuardarPregunta() {
        Pregunta pregunta = new Pregunta();
        pregunta.setConsigna("Pregunta test");

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

    @SuppressWarnings("unchecked")
    @Test
    public void debeListarTodasLasPreguntas() {
        Query<Pregunta> query = mock(Query.class);

        Pregunta p1 = new Pregunta();
        p1.setConsigna("P1");
        Pregunta p2 = new Pregunta();
        p2.setConsigna("P2");
        List<Pregunta> listaMock = List.of(p1, p2);

        when(session.createQuery("FROM Pregunta", Pregunta.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(listaMock);

        List<Pregunta> resultado = repository.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(session).createQuery("FROM Pregunta", Pregunta.class);
        verify(query).getResultList();
    }

    @Test
    void debeBuscarPreguntasPorCategoria() {

        String categoria = "Historia";

        List<Pregunta> resultadoEsperado = List.of(new Pregunta(), new Pregunta());

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        when(session.createQuery(
            "FROM Pregunta WHERE categoria = :categoria",
            Pregunta.class
        )).thenReturn(query);

        when(query.setParameter("categoria", categoria)).thenReturn(query);

        when(query.list()).thenReturn(resultadoEsperado);

        List<Pregunta> resultado = repository.buscarPorCategoria(categoria);

        assertEquals(2, resultado.size());
        assertEquals(resultadoEsperado, resultado);
    }

    @Test
    void debeDevolverTodasLasPreguntas() {

        List<Pregunta> lista = List.of(new Pregunta());

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        when(session.createQuery(anyString(), eq(Pregunta.class)))
        .thenReturn(query);

        when(query.getResultList())
        .thenReturn(lista);

        List<Pregunta> resultado = repository.findAll();

        assertEquals(1, resultado.size());
    }

    @Test
    void debeBuscarPorId() {

        Pregunta pregunta = new Pregunta();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Pregunta.class, 1L)).thenReturn(pregunta);

        Pregunta resultado = repository.findById(1L);

        assertNotNull(resultado);
    }

    @Test
    void debeEliminarSiExiste() {

        Pregunta pregunta = new Pregunta();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Pregunta.class, 1L)).thenReturn(pregunta);

        repository.deleteById(1L);

        verify(session).delete(pregunta);
    }
}