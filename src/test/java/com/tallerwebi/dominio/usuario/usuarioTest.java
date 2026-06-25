package com.tallerwebi.dominio.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class usuarioTest {

    @Test
    void deberiaCrearUsuarioConConstructorVacio() {
        Usuario usuario = new Usuario();

        assertNull(usuario.getEmail());
        assertNull(usuario.getUsername());
        assertNull(usuario.getPassword());
        assertNull(usuario.getRol());
        assertFalse(usuario.getActivo());
        assertEquals(0, usuario.getPuntaje());
        assertEquals(0, usuario.getPartidasGanadasSeguidas());
    }

    @Test
    void deberiaCrearUsuarioConConstructorParametrizado() {
        Usuario usuario = new Usuario("test@mail.com", "facu", "Pass123!");

        assertEquals("test@mail.com", usuario.getEmail());
        assertEquals("facu", usuario.getUsername());
        assertEquals("Pass123!", usuario.getPassword());
    }

    @Test
    void deberiaSetearEmailCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("mail@test.com");

        assertEquals("mail@test.com", usuario.getEmail());
    }

    @Test
    void deberiaSetearUsernameCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setUsername("usuario1");

        assertEquals("usuario1", usuario.getUsername());
    }

    @Test
    void deberiaSetearPasswordCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setPassword("123456");

        assertEquals("123456", usuario.getPassword());
    }

    @Test
    void deberiaSetearRolCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setRol("ADMIN");

        assertEquals("ADMIN", usuario.getRol());
    }

    @Test
    void activar_deberiaPonerActivoEnTrue() {
        Usuario usuario = new Usuario();
        usuario.activar();

        assertTrue(usuario.getActivo());
    }

    @Test
    void deberiaSetearPuntajeCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setPuntaje(10);

        assertEquals(10, usuario.getPuntaje());
    }

    @Test
    void deberiaSetearRespuestasAcertadasSeguidasCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setPartidasGanadasSeguidas(3);

        assertEquals(3, usuario.getPartidasGanadasSeguidas());
    }

    @Test
    void deberiaSetearActivoCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setActivo(true);

        assertTrue(usuario.getActivo());
    }
    @Test
    void deberiaSetearActivoEnFalse() {
        Usuario usuario = new Usuario();
        usuario.setActivo(false);

        assertFalse(usuario.getActivo());
    }

    @Test
    void deberiaSetearIdCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setId(100L);

        assertEquals(100L, usuario.getId());
    }

        @Test
    public void debeAsignarYObtenerId() {
        Usuario usuario = new Usuario();

        usuario.setId(1L);
        assertEquals(1L, usuario.getId());
    }

    @Test
    public void debeAsignarUsername() {
        Usuario usuario = new Usuario();

        usuario.setUsername("tmansilla7");
        assertEquals("tmansilla7", usuario.getUsername());
    }

    @Test
    public void debeActivarUsuario() {
        Usuario usuario = new Usuario();

        usuario.setActivo(false);
        usuario.activar();

        assertTrue(usuario.getActivo());
    }

    @Test
    public void debeCambiarId() {
        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setId(2L);

        assertEquals(2L, usuario.getId());
    }
}
