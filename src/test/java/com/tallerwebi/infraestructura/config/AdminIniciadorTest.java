package com.tallerwebi.infraestructura.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.admin.AdminIniciador;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;

public class AdminIniciadorTest {

    private RepositoryUsuario repositoryUsuario;
    private AdminIniciador adminIniciador;

    @BeforeEach
    public void init() {
        this.repositoryUsuario = mock(RepositoryUsuario.class);
        this.adminIniciador = new AdminIniciador(repositoryUsuario);
    }

    @Test
    public void debeCrearAdminSiNoExiste() {

        // no existe admin en BD
        when(repositoryUsuario.buscar("admin@preguntados.com"))
                .thenReturn(null);

        adminIniciador.crearAdmin();

        // verifica que se guarde el admin
        verify(repositoryUsuario, times(1)).guardar(any(Usuario.class));
    }

    @Test
    public void noDebeCrearAdminSiYaExiste() {

        Usuario adminExistente = new Usuario();
        adminExistente.setEmail("admin@preguntados.com");

        // ya existe admin
        when(repositoryUsuario.buscar("admin@preguntados.com"))
                .thenReturn(adminExistente);

        adminIniciador.crearAdmin();

        // NO debe guardar nada nuevo
        verify(repositoryUsuario, never()).guardar(any(Usuario.class));
    }

    @Test
    public void debeCrearAdminConDatosCorrectos() {

        when(repositoryUsuario.buscar("admin@preguntados.com"))
                .thenReturn(null);

        adminIniciador.crearAdmin();

        // capturamos el usuario que se guarda
        verify(repositoryUsuario).guardar(argThat(usuario ->
                usuario.getEmail().equals("admin@preguntados.com") &&
                usuario.getUsername().equals("admin") &&
                usuario.getPassword().equals("Admin123!") &&
                usuario.getRol().equals("ADMIN") &&
                usuario.getActivo().equals(true)
        ));
    }
}