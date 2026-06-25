package com.tallerwebi.infraestructura.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.usuario.Usuario;

public class RepositoryUsuarioImplTest {

    private SessionFactory sessionFactory;
    private Session session;
    private Criteria criteria;

    private RepositoryUsuarioImpl repositoryUsuario;

    @BeforeEach
    void init() {

        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        criteria = mock(Criteria.class);

        repositoryUsuario = new RepositoryUsuarioImpl(sessionFactory);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @SuppressWarnings("deprecation")
    @Test
    void debeBuscarUsuarioPorEmailYPassword() {

        Usuario usuario = new Usuario();
        usuario.setEmail("test@mail.com");

        when(session.createCriteria(Usuario.class)).thenReturn(criteria);
        when(criteria.add(any())).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(usuario);

        Usuario resultado =
            repositoryUsuario.buscarUsuario(
                "test@mail.com",
                "1234"
            );

        assertNotNull(resultado);
        assertEquals("test@mail.com", resultado.getEmail());
    }

    @Test
    void debeGuardarUsuario() {

        Usuario usuario = new Usuario();

        repositoryUsuario.guardar(usuario);

        verify(session).save(usuario);
    }

    @SuppressWarnings("deprecation")
    @Test
    void debeBuscarPorEmail() {

        Usuario usuario = new Usuario();
        usuario.setEmail("test@mail.com");

        when(session.createCriteria(Usuario.class)).thenReturn(criteria);
        when(criteria.add(any())).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(usuario);

        Usuario resultado =
            repositoryUsuario.buscar("test@mail.com");

        assertNotNull(resultado);
        assertEquals("test@mail.com", resultado.getEmail());
    }

    @Test
    void debeModificarUsuario() {

        Usuario usuario = new Usuario();

        repositoryUsuario.modificar(usuario);

        verify(session).update(usuario);
    }

    @SuppressWarnings("deprecation")
    @Test
    void debeBuscarPorUsername() {

        Usuario usuario = new Usuario();
        usuario.setUsername("usuarioTest");

        when(session.createCriteria(Usuario.class)).thenReturn(criteria);
        when(criteria.add(any())).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(usuario);

        Usuario resultado =
            repositoryUsuario.buscarPorUsername("usuarioTest");

        assertNotNull(resultado);
        assertEquals("usuarioTest", resultado.getUsername());
    }

    @SuppressWarnings("deprecation")
    @Test
    void debeListarTodosLosUsuarios() {

        List<Usuario> usuarios = new ArrayList<>();

        usuarios.add(new Usuario());
        usuarios.add(new Usuario());

        when(session.createCriteria(Usuario.class)).thenReturn(criteria);
        when(criteria.list()).thenReturn(usuarios);

        List<Usuario> resultado =
            repositoryUsuario.listarTodos();

        assertEquals(2, resultado.size());
     }
}