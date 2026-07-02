package com.tallerwebi.presentacion;

<<<<<<< HEAD
=======

>>>>>>> 363290f1c377ef3671b9698aae304ae645d1bccc
import com.tallerwebi.dominio.categoriaDia.CategoriaService;
import com.tallerwebi.dominio.juego.Respuesta;
import com.tallerwebi.dominio.juego.ServicioJuego;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.dominio.pregunta.PreguntaService;
import com.tallerwebi.dominio.registro.DatosRegistroDTO;
import com.tallerwebi.dominio.registro.ServicioRegistro;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.registro.ControladorRegistro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ControladorJuegoTest {

    private ServicioJuego servicioJuego;
    private CategoriaService categoriaService;
    private RepositoryUsuario repositoryUsuario;
    private PreguntaService preguntaService;

    private HttpServletRequest request;
    private HttpSession session;

    private ControladorJuego controller;

    @BeforeEach
    public void init() {

        this.servicioJuego = mock(ServicioJuego.class);
        this.categoriaService = mock(CategoriaService.class);
        this.repositoryUsuario = mock(RepositoryUsuario.class);
        this.preguntaService = mock(PreguntaService.class);

        this.request = mock(HttpServletRequest.class);
        this.session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        this.controller = new ControladorJuego(
            servicioJuego,
            preguntaService,
            categoriaService,
            repositoryUsuario
        );
    }

    // =========================
    // GET /buscar-juego
    // =========================

    @Test
    public void debeMostrarFormularioSeleccionCategoria() {

        ModelAndView mv = controller.buscarJuego();

        assertEquals("elegir-categoria", mv.getViewName());
        assertTrue(mv.getModel().containsKey("pregunta"));
    }

    // =========================
    // POST /juego
    // =========================

    @Test
    public void debeMostrarErrorSiNoHayPreguntas() {

        when(categoriaService.obtenerCategoriaActiva()).thenReturn("GENERAL");
        when(preguntaService.obtenerPorCategoria("GENERAL")).thenReturn(new ArrayList<>());

        Pregunta dto = new Pregunta();

        ModelAndView mv = controller.mostrarJuego(dto, request);

        assertEquals("elegir-categoria", mv.getViewName());
        assertEquals("No existen preguntas para la categoría del día",
            mv.getModel().get("error"));
    }

    @Test
    public void debeCargarJuegoCorrectamente() {

        when(categoriaService.obtenerCategoriaActiva()).thenReturn("GENERAL");

        Pregunta p = new Pregunta();
        p.setConsigna("Pregunta?");
        p.setCorrecta("A");

        List<Pregunta> lista = new ArrayList<>();
        lista.add(p);

        when(preguntaService.obtenerPorCategoria("GENERAL")).thenReturn(lista);

        when(session.getAttribute("usuario")).thenReturn(null);

        ModelAndView mv = controller.mostrarJuego(new Pregunta(), request);

        assertEquals("juego", mv.getViewName());

        verify(session).setAttribute(eq("preguntas"), any());
        verify(session).setAttribute(eq("indiceActual"), eq(0));
        verify(session).setAttribute(eq("partida"), any(Partida.class));
    }

    @Test
    public void debeGuardarUsuarioEnPartidaSiExiste() {

        Usuario u = new Usuario();
        u.setPuntaje(100);
        u.setPartidasGanadasSeguidas(3);

        when(session.getAttribute("usuario")).thenReturn(u);
        when(categoriaService.obtenerCategoriaActiva()).thenReturn("GENERAL");

        Pregunta p = new Pregunta();
        p.setCorrecta("A");

        when(preguntaService.obtenerPorCategoria("GENERAL"))
            .thenReturn(List.of(p));

        ModelAndView mv = controller.mostrarJuego(new Pregunta(), request);

        assertEquals("juego", mv.getViewName());
        assertEquals(3, mv.getModel().get("racha"));
        assertEquals(100, mv.getModel().get("puntaje"));
    }

    // =========================
    // POST /responder
    // =========================

    @Test
    public void debePasarSiguientePregunta() {

        Pregunta p = new Pregunta();
        p.setCorrecta("A");

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(p);
        preguntas.add(p);

        when(session.getAttribute("preguntas")).thenReturn(preguntas);
        when(session.getAttribute("indiceActual")).thenReturn(0);

        Partida partida = new Partida();
        partida.setRespuesta(new Respuesta());

        when(session.getAttribute("partida")).thenReturn(partida);

        ModelAndView mv = controller.responder("A", request);

        assertEquals("juego", mv.getViewName());

        verify(session).setAttribute(eq("indiceActual"), eq(1));
    }

    @Test
    public void debeFinalizarSiUltimaPregunta() {

        Pregunta p = new Pregunta();
        p.setCorrecta("A");

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(p);

        when(session.getAttribute("preguntas")).thenReturn(preguntas);
        when(session.getAttribute("indiceActual")).thenReturn(0);

        Partida partida = new Partida();
        partida.setRespuesta(new Respuesta());

        when(session.getAttribute("partida")).thenReturn(partida);

        ModelAndView mv = controller.responder("A", request);

        assertEquals("redirect:/partida-finalizada", mv.getViewName());
    }

    // =========================
    // GET /partida-finalizada
    // =========================

    @Test
    public void debeFinalizarPartidaYGuardar() {

        Pregunta p = new Pregunta();
        p.setCorrecta("A");

        List<Pregunta> preguntas = List.of(p);

        Partida partida = new Partida();
        partida.setRespuesta(new Respuesta());

        Usuario usuario = new Usuario();
        usuario.setPuntaje(10);
        usuario.setPartidasGanadasSeguidas(1);

        when(session.getAttribute("partida")).thenReturn(partida);
        when(session.getAttribute("preguntas")).thenReturn(preguntas);
        when(session.getAttribute("usuario")).thenReturn(usuario);

        when(servicioJuego.calcularPuntaje(any(), any())).thenReturn(50);
        when(servicioJuego.validarPartida(50)).thenReturn(true);

        ModelAndView mv = controller.partidaFinalizada(request);

        assertEquals("puntaje-final", mv.getViewName());
        assertEquals(50, mv.getModel().get("puntaje"));

        verify(repositoryUsuario).modificar(usuario);
        verify(servicioJuego).guardarPartida(partida);
    }

    @Test
    public void debeResetearRachaSiPierde() {

        Pregunta p = new Pregunta();
        List<Pregunta> preguntas = List.of(p);

        Partida partida = new Partida();
        partida.setRespuesta(new Respuesta());

        Usuario usuario = new Usuario();
        usuario.setPartidasGanadasSeguidas(5);
        usuario.setPuntaje(10);

        when(session.getAttribute("partida")).thenReturn(partida);
        when(session.getAttribute("preguntas")).thenReturn(preguntas);
        when(session.getAttribute("usuario")).thenReturn(usuario);

        when(servicioJuego.calcularPuntaje(any(), any())).thenReturn(10);
        when(servicioJuego.validarPartida(10)).thenReturn(false);

        controller.partidaFinalizada(request);

        assertEquals(0, usuario.getPartidasGanadasSeguidas());
    }

    @Test
    public void debeVolverAlFormularioSiOcurreUnaExcepcion() {

        ServicioRegistro servicioRegistro = mock(ServicioRegistro.class);
        ControladorRegistro controlador = new ControladorRegistro(servicioRegistro);

        DatosRegistroDTO datos = new DatosRegistroDTO();
        datos.setEmail("test@test.com");
        datos.setUsername("usuario");
        datos.setPassword("1234");

        doThrow(
            new IllegalArgumentException("El email ya existe")
        ).when(servicioRegistro).registrar(
            anyString(),
            anyString(),
            anyString()
        );

        ModelAndView modelAndView = controlador.registrar(datos);

        assertEquals(
            "formulario-registro-jugador",
            modelAndView.getViewName()
        );
        assertEquals(
            "El email ya existe",
            modelAndView.getModel().get("error")
        );
    }

    @Test
    public void debeRedirigirALoginCuandoElRegistroEsExitoso() {

        ServicioRegistro servicioRegistro = mock(ServicioRegistro.class);
        ControladorRegistro controlador = new ControladorRegistro(servicioRegistro);

        DatosRegistroDTO datos = new DatosRegistroDTO();
        datos.setEmail("test@test.com");
        datos.setUsername("usuario");
        datos.setPassword("1234");

        ModelAndView modelAndView = controlador.registrar(datos);

        verify(servicioRegistro).registrar(
            "test@test.com",
            "usuario",
            "1234"
        );

        assertEquals(
            "redirect:/login",
            modelAndView.getViewName()
        );
    }
}