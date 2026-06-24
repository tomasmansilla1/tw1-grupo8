package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.OpcionInvalidaException;
import com.tallerwebi.dominio.juego.RepositorioJuego;
import com.tallerwebi.dominio.juego.Respuesta;
import com.tallerwebi.dominio.juego.ServicioJuego;
import com.tallerwebi.dominio.login.ServicioJuegoImpl;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
