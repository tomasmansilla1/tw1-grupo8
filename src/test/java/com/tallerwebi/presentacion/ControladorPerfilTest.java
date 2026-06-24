package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorPerfilTest {

    private ControladorPerfil controladorPerfil;
    private RepositoryUsuario repositoryUsuarioMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init() {
        repositoryUsuarioMock = mock(RepositoryUsuario.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        controladorPerfil = new ControladorPerfil(repositoryUsuarioMock);
    }

    @Test
    public void queIrAPerfilRedireccioneALoginSiNoHayUsuarioEnSesion() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView mav = controladorPerfil.irAPerfil(requestMock);

        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queIrAPerfilMuestreLaVistaPerfilSiElUsuarioEstaLogueado() {
        Usuario usuario = new Usuario();
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);

        ModelAndView mav = controladorPerfil.irAPerfil(requestMock);

        assertThat(mav.getViewName(), equalToIgnoringCase("perfil"));
    }

    @Test
    public void queVerPerfilOtroRetorneLaVistaPerfilCuandoElUsuarioExiste() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(repositoryUsuarioMock.buscar(1L)).thenReturn(usuario);

        ModelAndView mav = controladorPerfil.verPerfilOtro(1L);

        assertThat(mav.getViewName(), equalToIgnoringCase("perfil"));

        assertThat(mav.getModel().get("usuario"), equalTo(usuario));
    }
}