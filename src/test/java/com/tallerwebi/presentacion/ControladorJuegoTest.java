package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Respuesta;
import com.tallerwebi.dominio.ServicioJuego;
import com.tallerwebi.dominio.excepcion.OpcionInvalidaException;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.dominio.pregunta.PreguntaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorJuegoTest {

    private ControladorJuego controladorJuego;
    private ServicioJuego servicioJuegoMock;

    @BeforeEach
    public void init() {
        servicioJuegoMock = mock(ServicioJuego.class);
        controladorJuego = new ControladorJuego(servicioJuegoMock);
    }

    @Test
    public void queBuscarJuegoRetorneLaVistaElegirCategoria() {
        ModelAndView mav = controladorJuego.buscarJuego();

        assertThat(mav.getViewName(), equalToIgnoringCase("elegir-categoria"));
    }

    @Test
    public void queBuscarJuegoContengaPreguntaDto() {
        ModelAndView mav = controladorJuego.buscarJuego();

        assertThat(mav.getModel().get("preguntaDto"), instanceOf(PreguntaDto.class));
    }

    @Test
    public void queMostrarJuegoRetorneVistaJuegoCuandoLaCategoriaEsValida() throws OpcionInvalidaException {
        PreguntaDto dto = new PreguntaDto();
        dto.setCategoria("Historia");

        Pregunta pregunta = new Pregunta();

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(pregunta);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        when(servicioJuegoMock.buscarPreguntas("Historia")).thenReturn(preguntas);

        ModelAndView mav = controladorJuego.mostrarJuego(dto, request);

        assertThat(mav.getViewName(), equalToIgnoringCase("juego"));
    }

    @Test
    public void queResponderMuestreLaSiguientePregunta() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        Pregunta pregunta1 = new Pregunta();
        Pregunta pregunta2 = new Pregunta();

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(pregunta1);
        preguntas.add(pregunta2);

        Respuesta respuesta = new Respuesta();
        Partida partida = new Partida();
        partida.setRespuesta(respuesta);

        when(session.getAttribute("preguntas")).thenReturn(preguntas);
        when(session.getAttribute("indiceActual")).thenReturn(0);
        when(session.getAttribute("partida")).thenReturn(partida);

        ModelAndView mav = controladorJuego.responder("A", request);

        assertThat(mav.getViewName(), equalToIgnoringCase("juego"));
    }

    @Test
    public void queResponderRedireccioneAPartidaFinalizadaCuandoNoHayMasPreguntas() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        Pregunta pregunta = new Pregunta();

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(pregunta);

        Respuesta respuesta = new Respuesta();
        Partida partida = new Partida();
        partida.setRespuesta(respuesta);

        when(session.getAttribute("preguntas")).thenReturn(preguntas);
        when(session.getAttribute("indiceActual")).thenReturn(0);
        when(session.getAttribute("partida")).thenReturn(partida);

        ModelAndView mav = controladorJuego.responder("A", request);

        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/partida-finalizada"));
    }

    @Test
    public void quePartidaFinalizadaRetorneVistaPuntajeFinal() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        Partida partida = new Partida();
        partida.setRespuesta(new Respuesta());

        List<Pregunta> preguntas = new ArrayList<>();

        when(session.getAttribute("partida")).thenReturn(partida);
        when(session.getAttribute("preguntas")).thenReturn(preguntas);

        when(servicioJuegoMock.calcularPuntaje(preguntas, partida.getRespuesta())).thenReturn(8);

        when(servicioJuegoMock.validarPartida(8)).thenReturn(true);

        ModelAndView mav = controladorJuego.partidaFinalizada(request);

        assertThat(mav.getViewName(), equalToIgnoringCase("puntaje-final"));
    }
}
