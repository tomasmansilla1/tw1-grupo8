package com.tallerwebi.infraestructura.pregunta;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.registro.ServicioRegistroImpl;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;

public class ServicioRegistroImplTest {

    private RepositoryUsuario repositorioUsuario;
    private ServicioRegistroImpl servicio;

    @BeforeEach
    public void setUp() {
        this.repositorioUsuario = mock(RepositoryUsuario.class);
        this.servicio = new ServicioRegistroImpl(repositorioUsuario);
    }

    // =========================
    // REGISTRO EXITOSO
    // =========================

    @Test
    public void debeRegistrarUsuarioCorrectamente() {

        when(repositorioUsuario.buscar("test@mail.com")).thenReturn(null);
        when(repositorioUsuario.buscarPorUsername("juan")).thenReturn(null);

        Usuario usuario = servicio.registrar(
            "test@mail.com",
            "juan",
            "Password1!"
        );

        assertNotNull(usuario);
        assertEquals("test@mail.com", usuario.getEmail());
        assertEquals("juan", usuario.getUsername());

        verify(repositorioUsuario).guardar(usuario);
    }

    // =========================
    // EMAIL YA EXISTE
    // =========================

    @Test
    public void debeFallarSiEmailYaExiste() {

        when(repositorioUsuario.buscar("test@mail.com"))
            .thenReturn(new Usuario());

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> servicio.registrar("test@mail.com", "juan", "Password1!")
        );

        assertEquals("El email ya existe", ex.getMessage());

        verify(repositorioUsuario, never()).guardar(any());
    }

    // =========================
    // USERNAME YA EXISTE
    // =========================

    @Test
    public void debeFallarSiUsernameYaExiste() {

        when(repositorioUsuario.buscar("test@mail.com")).thenReturn(null);
        when(repositorioUsuario.buscarPorUsername("juan"))
            .thenReturn(new Usuario());

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> servicio.registrar("test@mail.com", "juan", "Password1!")
        );

        assertEquals("El username ya existe", ex.getMessage());

        verify(repositorioUsuario, never()).guardar(any());
    }

    // =========================
    // EMAIL VACÍO
    // =========================

    @Test
    public void debeFallarSiEmailVacio() {

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> servicio.registrar("", "juan", "Password1!")
        );

        assertEquals("El email no puede estar vacío", ex.getMessage());
    }

    // =========================
    // EMAIL FORMATO INVALIDO
    // =========================

    @Test
    public void debeFallarSiEmailFormatoIncorrecto() {

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> servicio.registrar("correo-invalido", "juan", "Password1!")
        );

        assertEquals("El email no tiene un formato valido", ex.getMessage());
    }

    // =========================
    // USERNAME VACÍO
    // =========================

    @Test
    public void debeFallarSiUsernameVacio() {

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> servicio.registrar("test@mail.com", "", "Password1!")
        );

        assertEquals("El username no puede estar vacío", ex.getMessage());
    }

    // =========================
    // PASSWORD VACÍA
    // =========================

    @Test
    public void debeFallarSiPasswordVacia() {

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> servicio.registrar("test@mail.com", "juan", "")
        );

        assertEquals("El password no puede estar vacío", ex.getMessage());
    }

    // =========================
    // PASSWORD INVÁLIDA
    // =========================

    @Test
    public void debeFallarSiPasswordNoCumpleFormato() {

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> servicio.registrar("test@mail.com", "juan", "abc")
        );

        assertEquals(
            "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial",
            ex.getMessage()
        );
    }
}