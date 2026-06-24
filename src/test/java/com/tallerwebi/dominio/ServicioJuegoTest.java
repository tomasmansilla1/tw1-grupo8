package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.OpcionInvalidaException;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ServicioJuegoTest {

    private ServicioJuego servicioJuego;
    private RepositorioJuego repositorioJuegoMock;

    @BeforeEach
    public void init() {
        repositorioJuegoMock = mock(RepositorioJuego.class);
        servicioJuego = new ServicioJuegoImpl(repositorioJuegoMock);
    }

    @Test
    public void queBuscarPreguntasPorCategoriaRetorneListaDePreguntas() {
        String categoria = "Historia";

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(new Pregunta());

        when(repositorioJuegoMock.buscarPreguntaPorCategoria(categoria))
                .thenReturn(preguntas);

        List<Pregunta> resultado = servicioJuego.buscarPreguntas(categoria);

        assertThat(resultado.size(), equalTo(1));
    }

    @Test
    public void queBuscarPreguntasSinLanceExcepcionCuandoCategoriaEsVacia() {
        assertThrows(OpcionInvalidaException.class,() -> servicioJuego.buscarPreguntas("")
        );
    }

    @Test
    public void queCalcularPuntajeRetorneDiezCuandoHayDosRespuestasCorrectas() {
        Pregunta pregunta1 = new Pregunta();
        pregunta1.setCorrecta("A");

        Pregunta pregunta2 = new Pregunta();
        pregunta2.setCorrecta("B");

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(pregunta1);
        preguntas.add(pregunta2);

        Respuesta respuesta = new Respuesta();
        respuesta.getRespuestasUsuario().add("A");
        respuesta.getRespuestasUsuario().add("B");

        Integer puntaje = servicioJuego.calcularPuntaje(preguntas, respuesta);

        assertThat(puntaje, equalTo(10));
    }

    @Test
    public void queGuardarPartidaLlameAlRepositorio() {
        Partida partida = new Partida();

        servicioJuego.guardarPartida(partida);

        verify(repositorioJuegoMock, times(1)).guardarPartida(partida);
    }

    @Test
    public void queValidarPartidaRetorneTrueCuandoElPuntajeEsMayorAOIgualA35() {
        Boolean resultado = servicioJuego.validarPartida(35);

        assertThat(resultado, equalTo(true));
    }

    @Test
    public void validarPartidaDebeRetornarTrueSiPuntajeEsMayorOIgualA35() {

        ServicioJuegoImpl servicio = new ServicioJuegoImpl(Mockito.mock(RepositorioJuego.class));

        Boolean resultado = servicio.validarPartida(35);
        assertTrue(resultado);
    }

    @Test
    public void validarPartidaDebeRetornarFalseSiPuntajeEsMenorA35() {

        ServicioJuegoImpl servicio = new ServicioJuegoImpl(Mockito.mock(RepositorioJuego.class));

        Boolean resultado = servicio.validarPartida(30);
        assertFalse(resultado);
    }

    @Test
    public void calcularRespuestasCorrectasDebeRetornarDos() {
        ServicioJuegoImpl servicio = new ServicioJuegoImpl(Mockito.mock(RepositorioJuego.class));

        Pregunta p1 = new Pregunta(
            "Ciencia", "P1",
            "A", "B", "C", "D", "A"
        );
        Pregunta p2 = new Pregunta(
            "Historia", "P2",
            "A", "B", "C", "D", "C"
        );

        List<Pregunta> preguntas = Arrays.asList(p1, p2);

        Respuesta respuesta = new Respuesta();
        respuesta.setRespuestasUsuario(Arrays.asList("A", "C"));

        Integer resultado = servicio.calcularRespuestasCorrectas(preguntas, respuesta);

        assertEquals(2, resultado);
    }   

    @Test
    public void calcularRespuestasCorrectasDebeRetornarUna() {
        ServicioJuegoImpl servicio = new ServicioJuegoImpl(Mockito.mock(RepositorioJuego.class));

        Pregunta p1 = new Pregunta(
            "Ciencia", "P1",
            "A", "B", "C", "D", "A"
        );

        Pregunta p2 = new Pregunta(
            "Historia", "P2",
            "A", "B", "C", "D", "C"
        );

        List<Pregunta> preguntas = Arrays.asList(p1, p2);

        Respuesta respuesta = new Respuesta();
        respuesta.setRespuestasUsuario(Arrays.asList("A", "B"));

        Integer resultado = servicio.calcularRespuestasCorrectas(preguntas, respuesta);

        assertEquals(1, resultado);
    }
}
