package com.tallerwebi.infraestructura.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.tallerwebi.dominio.juego.ServicioJuego;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.historialUsuario.ControladorHistorial;

@Rollback
@Transactional
public class ControladorHistorialTest {

    @Mock
    private ServicioJuego servicioJuego;

    private ControladorHistorial controlador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controlador = new ControladorHistorial(servicioJuego);
    }

     @Test
    public void debeRedirigirALoginSiNoHayUsuarioEnSesion() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usuario")).thenReturn(null);

        ModelAndView modelAndView = controlador.historial(request);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    public void debeMostrarHistorialDelUsuarioLogueado() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        List<Partida> partidas = new ArrayList<>();

        Partida p1 = new Partida();
        p1.setPuntajeObtenido(40);

        Partida p2 = new Partida();
        p2.setPuntajeObtenido(35);

        partidas.add(p1);
        partidas.add(p2);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usuario")).thenReturn(usuario);

        when(servicioJuego.buscarHistorial(1L)).thenReturn(partidas);

        ModelAndView modelAndView = controlador.historial(request);

        assertEquals("historial", modelAndView.getViewName());
        assertEquals(partidas, modelAndView.getModel().get("partidas"));
    }
}