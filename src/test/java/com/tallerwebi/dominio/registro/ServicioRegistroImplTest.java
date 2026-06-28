package com.tallerwebi.dominio.registro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Roles;
import com.tallerwebi.dominio.usuario.Usuario;

public class ServicioRegistroImplTest {

    private RepositoryUsuario repositorioUsuario;
    private ServicioRegistroImpl servicioRegistro;

    @BeforeEach
    public void init() {
        repositorioUsuario = Mockito.mock(RepositoryUsuario.class);
        servicioRegistro = new ServicioRegistroImpl(repositorioUsuario);
    }

    @Test
    public void registrarUsuarioCorrectamente() {

        when(repositorioUsuario.buscar("test@mail.com"))
                .thenReturn(null);

        when(repositorioUsuario.buscarPorUsername("usuario"))
                .thenReturn(null);

        Usuario usuario = servicioRegistro.registrar(
                "test@mail.com",
                "usuario",
                "Pass123!"
        );

        assertNotNull(usuario);
        assertEquals("test@mail.com", usuario.getEmail());
        assertEquals("usuario", usuario.getUsername());
        assertEquals(Roles.JUGADOR, usuario.getRol());
        assertTrue(usuario.getActivo());
        assertEquals(0, usuario.getPuntaje());
        assertEquals(0, usuario.getPartidasGanadasSeguidas());

        verify(repositorioUsuario).guardar(usuario);
    }

    @Test
    public void noDebeRegistrarSiEmailYaExiste() {

        Usuario existente = new Usuario();

        when(repositorioUsuario.buscar("test@mail.com")).thenReturn(existente);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> servicioRegistro.registrar(
                "test@mail.com",
                "usuario",
                "Pass123!"
            )
        );

        assertEquals("El email ya existe", ex.getMessage());
        verify(repositorioUsuario, never()).guardar(any());
    }

    @Test
    public void noDebeRegistrarSiUsernameYaExiste() {

        Usuario existente = new Usuario();

        when(repositorioUsuario.buscar("test@mail.com")).thenReturn(null);
        when(repositorioUsuario.buscarPorUsername("usuario")).thenReturn(existente);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> servicioRegistro.registrar(
                "test@mail.com",
                "usuario",
                "Pass123!"
            )
        );

        assertEquals("El username ya existe", ex.getMessage());
        verify(repositorioUsuario, never()).guardar(any());
    }

    @Test
    public void noDebeRegistrarConEmailInvalido() {

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> servicioRegistro.registrar(
                "email-invalido",
                "usuario",
                "Pass123!"
            )
        );
        assertEquals("El email no tiene un formato valido", ex.getMessage());
    }

    @Test
    public void noDebeRegistrarConPasswordInvalida() {

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> servicioRegistro.registrar(
                "test@mail.com",
                "usuario",
                "123"
            )
        );
        assertEquals("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial",ex.getMessage());
    }
}