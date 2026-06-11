package com.tallerwebi.infraestructura.categoriaDia;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.categoriaDia.CategoriaHistorial;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CategoriaRepositoryImplTest {
    
    private SessionFactory sessionFactoryMock;
    private Session sessionMock;

    private Query<CategoriaHistorial> queryMock;

    private CategoriaRepositoryImpl categoriaRepository;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void init() {
        sessionFactoryMock = mock(SessionFactory.class);
        sessionMock = mock(Session.class);
        queryMock = mock(Query.class);

        categoriaRepository = new CategoriaRepositoryImpl(sessionFactoryMock);

        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
    }

    @Test
    public void findUltimaDeberiaRetornarUltimaCategoria() {
        CategoriaHistorial categoria = new CategoriaHistorial();

        when(sessionMock.createQuery(
            anyString(),
            eq(CategoriaHistorial.class)
        )).thenReturn(queryMock);

        when(queryMock.setMaxResults(1)).thenReturn(queryMock);
        when(queryMock.uniqueResult()).thenReturn(categoria);

        CategoriaHistorial resultado = categoriaRepository.findUltima();

        assertEquals(categoria, resultado);

        verify(
            sessionFactoryMock, 
            times(1)
        ).getCurrentSession();

        verify(
            sessionMock, 
            times(1)
        ).createQuery(
            "FROM CategoriaHistorial c ORDER BY c.fecha DESC",
            CategoriaHistorial.class
        );

        verify(
            queryMock, 
            times(1)
        ).setMaxResults(1);
        verify(
            queryMock, 
            times(1)
        ).uniqueResult();
    }

    @Test
    public void findAllDeberiaRetornarTodasLasCategorias() {
        CategoriaHistorial categoria = new CategoriaHistorial();

        List<CategoriaHistorial> lista = singletonList(categoria);

        when(sessionMock.createQuery(
            anyString(),
            eq(CategoriaHistorial.class)
        )).thenReturn(queryMock);

        when(queryMock.list()).thenReturn(lista);

        List<CategoriaHistorial> resultado = categoriaRepository.findAll();

        assertThat(resultado, equalTo(lista));
        verify(sessionFactoryMock).getCurrentSession();

        verify(sessionMock).createQuery(
            "FROM CategoriaHistorial c ORDER BY c.fecha DESC",
            CategoriaHistorial.class
        );
        verify(queryMock).list();
    }

    @Test
    public void saveDeberiaGuardarCategoria() {
        CategoriaHistorial categoria = new CategoriaHistorial();
        categoriaRepository.save(categoria);

        verify(
            sessionFactoryMock, 
            times(1)
        ).getCurrentSession();
        
        verify(
            sessionMock, 
            times(1)
        ).save(categoria);
    }
}