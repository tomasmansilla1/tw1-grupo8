package test.presentacion;

import com.tallerwebi.dominio.Categoria.Categoria;
import com.tallerwebi.dominio.apiPregunta.ApiPregunta;
import com.tallerwebi.dominio.categoriaDia.CategoriaHistorial;
import com.tallerwebi.dominio.categoriaDia.CategoriaService;
import com.tallerwebi.dominio.juego.Respuesta;
import com.tallerwebi.dominio.juego.ServicioJuego;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.servicioCategoria.ServicioCategoria;
import com.tallerwebi.dominio.servicioPregunta.PreguntaApiService;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ControllerCategoriaTest {

    private ControllerCategoria controllerCategoria;

    private PreguntaApiService preguntaService;
    private CategoriaService categoriaDiaService;
    private ServicioCategoria servicioCategoria;
    private ServicioJuego servicioJuego;

    private HttpServletRequest request;
    private HttpSession session;

    @Before
    public void init() throws Exception {
        controllerCategoria = new ControllerCategoria();

        preguntaService = mock(PreguntaApiService.class);
        categoriaDiaService = mock(CategoriaService.class);
        servicioCategoria = mock(ServicioCategoria.class);
        servicioJuego = mock(ServicioJuego.class);

        inyectarCampoPrivado("preguntaService", preguntaService);
        inyectarCampoPrivado("categoriaDiaService", categoriaDiaService);
        inyectarCampoPrivado("servicioCategoria", servicioCategoria);
        inyectarCampoPrivado("servicioJuego", servicioJuego);

        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
    }

    private void inyectarCampoPrivado(String nombreCampo, Object valor) throws Exception {
        java.lang.reflect.Field campo = ControllerCategoria.class.getDeclaredField(nombreCampo);
        campo.setAccessible(true);
        campo.set(controllerCategoria, valor);
    }

    private Usuario crearUsuario(String username) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        return usuario;
    }

    @Test
    public void inicioRedirigeALoginSiNoHayUsuarioEnSesion() {
        when(session.getAttribute("usuario")).thenReturn(null);

        ModelAndView mav = controllerCategoria.inicio(request);

        assertThat(mav.getViewName(), equalTo("redirect:/login"));
    }

    @Test
    public void inicioMuestraPantallaInicialConDatosDelUsuario() {
        Usuario usuario = crearUsuario("tomas");
        when(session.getAttribute("usuario")).thenReturn(usuario);
        when(servicioCategoria.obtenerTotal()).thenReturn(20);

        ModelAndView mav = controllerCategoria.inicio(request);

        assertThat(mav.getViewName(), equalTo("categoria-inicio"));
        assertThat((Integer) mav.getModel().get("puntaje"), equalTo(0));
        assertThat((Integer) mav.getModel().get("categoriasUsadas"), equalTo(0));
        assertThat((Integer) mav.getModel().get("totalCategorias"), equalTo(20));
        assertThat((Integer) mav.getModel().get("categoriasRestantes"), equalTo(20));
        assertThat((String) mav.getModel().get("nombreUsuario"), equalTo("tomas"));

        verify(session).setAttribute(eq("categoriasUsadas"), anyList());
        verify(session).setAttribute("puntaje", 0);
    }

    @Test
    public void obtenerCategoriaUsaDiezPorDefectoSiCantidadEsInvalida() {
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("usuario")).thenReturn(null);

        CategoriaHistorial historial = new CategoriaHistorial();
        historial.setApiIdNombre(1);
        historial.setNombre("Historia");
        when(categoriaDiaService.obtenerIdApiPregunta()).thenReturn(historial);

        Categoria categoria = new Categoria();
        categoria.setId(1);
        when(servicioCategoria.obtenerPorId(1)).thenReturn(categoria);

        ApiPregunta pregunta = new ApiPregunta();
        when(preguntaService.obtenerPreguntasPorCategoria(10, 1))
                .thenReturn(Collections.singletonList(pregunta));

        controllerCategoria.obtenerCategoria(999, request);

        verify(preguntaService).obtenerPreguntasPorCategoria(10, 1);
    }

    @Test
    public void obtenerCategoriaDevuelveCategoriaFinalSiNoHayMasCategorias() {
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("usuario")).thenReturn(null);

        CategoriaHistorial historial = new CategoriaHistorial();
        historial.setApiIdNombre(1);
        when(categoriaDiaService.obtenerIdApiPregunta()).thenReturn(historial);
        when(servicioCategoria.obtenerPorId(1)).thenReturn(null);
        when(servicioCategoria.obtenerTotal()).thenReturn(15);

        ModelAndView mav = controllerCategoria.obtenerCategoria(10, request);

        assertThat(mav.getViewName(), equalTo("categoria-final"));
        assertThat((Integer) mav.getModel().get("puntajeFinal"), equalTo(0));
        assertThat((Integer) mav.getModel().get("totalCategorias"), equalTo(15));
    }

    @Test
    public void obtenerCategoriaSaltaCategoriasSinPreguntasHastaEncontrarUnaConPreguntas() {
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("usuario")).thenReturn(null);

        CategoriaHistorial historial = new CategoriaHistorial();
        historial.setApiIdNombre(1);
        when(categoriaDiaService.obtenerIdApiPregunta()).thenReturn(historial);

        Categoria categoriaSinPreguntas = new Categoria();
        categoriaSinPreguntas.setId(1);
        when(servicioCategoria.obtenerPorId(1)).thenReturn(categoriaSinPreguntas);

        ApiPregunta pregunta = new ApiPregunta();
        when(preguntaService.obtenerPreguntasPorCategoria(10, 1))
                .thenReturn(Collections.<ApiPregunta>emptyList())
                .thenReturn(Collections.singletonList(pregunta));

        ModelAndView mav = controllerCategoria.obtenerCategoria(10, request);

        assertThat(mav.getViewName(), equalTo("categoria-pregunta"));
        verify(preguntaService, times(2)).obtenerPreguntasPorCategoria(10, 1);
    }

    @Test
    public void obtenerCategoriaExitosaGuardaEstadoDePartidaYDevuelveVistaDePregunta() {
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("puntaje")).thenReturn(0);
        Usuario usuario = crearUsuario("tomas");
        when(session.getAttribute("usuario")).thenReturn(usuario);

        CategoriaHistorial historial = new CategoriaHistorial();
        historial.setApiIdNombre(2);
        historial.setNombre("Deportes");
        when(categoriaDiaService.obtenerIdApiPregunta()).thenReturn(historial);

        Categoria categoria = new Categoria();
        categoria.setId(2);
        when(servicioCategoria.obtenerPorId(2)).thenReturn(categoria);

        ApiPregunta pregunta = new ApiPregunta();
        when(preguntaService.obtenerPreguntasPorCategoria(5, 2))
                .thenReturn(Collections.singletonList(pregunta));

        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(5);
        when(session.getAttribute("preguntasRespondidas")).thenReturn(0);

        ModelAndView mav = controllerCategoria.obtenerCategoria(5, request);

        assertThat(mav.getViewName(), equalTo("categoria-pregunta"));
        assertThat((ApiPregunta) mav.getModel().get("pregunta"), is(pregunta));
        assertThat((Categoria) mav.getModel().get("categoria"), is(categoria));
        assertThat((Integer) mav.getModel().get("preguntasRespondidas"), equalTo(1));

        verify(session).setAttribute(eq("partida"), any(Partida.class));
        verify(session).setAttribute("preguntas", Collections.singletonList(pregunta));
        verify(session).setAttribute("indicePregunta", 0);
    }

    @Test
    public void siguientePreguntaRedirigeSiNoHayCategoriaActual() {
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");
        when(session.getAttribute("categoriaActualId")).thenReturn(null);

        ModelAndView mav = controllerCategoria.siguientePregunta(request);

        assertThat(mav.getViewName(), equalTo("redirect:/categoria"));
    }

    @Test
    public void siguientePreguntaRedirigeSiNoHayPreguntasCargadas() {
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");
        when(session.getAttribute("categoriaActualId")).thenReturn(3);
        when(session.getAttribute("preguntas")).thenReturn(null);

        ModelAndView mav = controllerCategoria.siguientePregunta(request);

        assertThat(mav.getViewName(), equalTo("redirect:/categoria"));
    }

    @Test
    public void siguientePreguntaRedirigeSiElIndiceSuperaLaCantidadDePreguntas() {
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");
        when(session.getAttribute("categoriaActualId")).thenReturn(3);

        List<ApiPregunta> preguntas = Collections.singletonList(new ApiPregunta());
        when(session.getAttribute("preguntas")).thenReturn(preguntas);
        when(session.getAttribute("indicePregunta")).thenReturn(0); // única pregunta, índice 0

        ModelAndView mav = controllerCategoria.siguientePregunta(request);

        assertThat(mav.getViewName(), equalTo("redirect:/categoria"));
    }

    @Test
    public void siguientePreguntaAvanzaCorrectamenteALaSiguientePregunta() {
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("puntaje")).thenReturn(10);
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");
        when(session.getAttribute("categoriaActualId")).thenReturn(3);
        when(session.getAttribute("preguntasRespondidas")).thenReturn(1);
        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(5);

        ApiPregunta pregunta0 = new ApiPregunta();
        ApiPregunta pregunta1 = new ApiPregunta();
        when(session.getAttribute("preguntas")).thenReturn(Arrays.asList(pregunta0, pregunta1));
        when(session.getAttribute("indicePregunta")).thenReturn(0);

        Categoria categoria = new Categoria();
        categoria.setId(3);
        when(servicioCategoria.obtenerPorId(3)).thenReturn(categoria);

        ModelAndView mav = controllerCategoria.siguientePregunta(request);

        assertThat(mav.getViewName(), equalTo("categoria-pregunta"));
        assertThat((ApiPregunta) mav.getModel().get("pregunta"), is(pregunta1));
        verify(session).setAttribute("indicePregunta", 1);
        verify(session).setAttribute("preguntaActual", pregunta1);
    }

    private Partida crearPartidaConRespuestas() {
        Partida partida = new Partida();
        Respuesta respuesta = new Respuesta();
        respuesta.setRespuestasUsuario(new ArrayList<>());
        partida.setRespuesta(respuesta);
        return partida;
    }

    @Test
    public void responderRedirigeSiNoHayPreguntaActualEnSesion() {
        when(session.getAttribute("partida")).thenReturn(crearPartidaConRespuestas());
        when(session.getAttribute("preguntaActual")).thenReturn(null);
        when(session.getAttribute("categoriaActualId")).thenReturn(1);

        ModelAndView mav = controllerCategoria.responder("cualquiera", 1, request);

        assertThat(mav.getViewName(), equalTo("redirect:/categoria"));
    }

    @Test
    public void responderRedirigeSiElCategoriaIdNoCoincideConElDeSesion() {
        ApiPregunta pregunta = new ApiPregunta();
        when(session.getAttribute("partida")).thenReturn(crearPartidaConRespuestas());
        when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
        when(session.getAttribute("categoriaActualId")).thenReturn(1);

        ModelAndView mav = controllerCategoria.responder("cualquiera", 2, request);

        assertThat(mav.getViewName(), equalTo("redirect:/categoria"));
    }

    @Test
    public void responderSumaPuntosCuandoLaRespuestaEsCorrectaYNoEsLaUltimaPregunta() {
        ApiPregunta pregunta = mock(ApiPregunta.class);
        when(pregunta.getRespuestaCorrectaDecodificada()).thenReturn("Buenos Aires");

        when(session.getAttribute("partida")).thenReturn(crearPartidaConRespuestas());
        when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
        when(session.getAttribute("categoriaActualId")).thenReturn(1);
        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(5);
        when(session.getAttribute("preguntasRespondidas")).thenReturn(0);
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");

        Categoria categoria = new Categoria();
        categoria.setId(1);
        when(servicioCategoria.obtenerPorId(1)).thenReturn(categoria);

        ModelAndView mav = controllerCategoria.responder("Buenos Aires", 1, request);

        assertThat(mav.getViewName(), equalTo("categoria-resultado"));
        assertThat((Boolean) mav.getModel().get("acierto"), is(true));
        verify(session).setAttribute("puntaje", 10);
        verify(session).setAttribute("preguntasRespondidas", 1);
        verifyNoInteractions(servicioJuego);
    }

    @Test
    public void responderNoSumaPuntosYExponeRespuestaCorrectaCuandoFalla() {
        ApiPregunta pregunta = mock(ApiPregunta.class);
        when(pregunta.getRespuestaCorrectaDecodificada()).thenReturn("Buenos Aires");

        when(session.getAttribute("partida")).thenReturn(crearPartidaConRespuestas());
        when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
        when(session.getAttribute("categoriaActualId")).thenReturn(1);
        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(5);
        when(session.getAttribute("preguntasRespondidas")).thenReturn(0);
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");

        Categoria categoria = new Categoria();
        categoria.setId(1);
        when(servicioCategoria.obtenerPorId(1)).thenReturn(categoria);

        ModelAndView mav = controllerCategoria.responder("Córdoba", 1, request);

        assertThat(mav.getViewName(), equalTo("categoria-resultado"));
        assertThat((Boolean) mav.getModel().get("acierto"), is(false));
        assertThat((String) mav.getModel().get("respuestaCorrecta"), equalTo("Buenos Aires"));
        verify(session, never()).setAttribute("puntaje", 10);
    }

    @Test
    public void responderFinalizaLaPartidaYLaGuardaCuandoEsLaUltimaPregunta() {
        ApiPregunta pregunta = mock(ApiPregunta.class);
        when(pregunta.getRespuestaCorrectaDecodificada()).thenReturn("Buenos Aires");

        Partida partida = crearPartidaConRespuestas();
        when(session.getAttribute("partida")).thenReturn(partida);
        when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
        when(session.getAttribute("categoriaActualId")).thenReturn(1);
        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(1);
        when(session.getAttribute("preguntasRespondidas")).thenReturn(0);
        when(session.getAttribute("puntaje")).thenReturn(20);
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");

        Categoria categoria = new Categoria();
        categoria.setId(1);
        when(servicioCategoria.obtenerPorId(1)).thenReturn(categoria);
        when(servicioCategoria.obtenerTotal()).thenReturn(12);

        ModelAndView mav = controllerCategoria.responder("Buenos Aires", 1, request);

        assertThat(mav.getViewName(), equalTo("categoria-final"));
        assertThat((Integer) mav.getModel().get("puntajeFinal"), equalTo(30));

        verify(servicioJuego).guardarPartida(partida);
        assertTrue(partida.isEsVictoria()); // 30 >= 30
        assertThat(partida.getPuntajeObtenido(), equalTo(30));
        verify(session).removeAttribute("preguntaActual");
        verify(session).removeAttribute("categoriaActualId");
    }

    @Test
    public void responderMarcaDerrotaCuandoElPuntajeFinalNoLlegaAlMinimo() {
        ApiPregunta pregunta = mock(ApiPregunta.class);
        when(pregunta.getRespuestaCorrectaDecodificada()).thenReturn("Buenos Aires");

        Partida partida = crearPartidaConRespuestas();
        when(session.getAttribute("partida")).thenReturn(partida);
        when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
        when(session.getAttribute("categoriaActualId")).thenReturn(1);
        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(1);
        when(session.getAttribute("preguntasRespondidas")).thenReturn(0);
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");

        Categoria categoria = new Categoria();
        categoria.setId(1);
        when(servicioCategoria.obtenerPorId(1)).thenReturn(categoria);

        controllerCategoria.responder("Córdoba", 1, request);

        assertFalse(partida.isEsVictoria());
        verify(servicioJuego).guardarPartida(partida);
    }

    @Test
    public void siguienteCategoriaDevuelveCategoriaFinalCuandoNoQuedanCategorias() {
        List<Integer> usadas = new ArrayList<>(Arrays.asList(1, 2, 3));
        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(10);
        when(session.getAttribute("puntaje")).thenReturn(40);
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");
        when(session.getAttribute("categoriasUsadas")).thenReturn(usadas);
        when(servicioCategoria.obtenerCategoriaRandom(usadas)).thenReturn(null);
        when(servicioCategoria.obtenerTotal()).thenReturn(3);

        ModelAndView mav = controllerCategoria.siguienteCategoria(request);

        assertThat(mav.getViewName(), equalTo("categoria-final"));
        assertThat((Integer) mav.getModel().get("puntajeFinal"), equalTo(40));
    }

    @Test
    public void siguienteCategoriaUsaDiezPorDefectoSiNoHayCantidadEnSesion() {
        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(null);
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("nombreUsuario")).thenReturn(null);
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());

        Categoria categoria = new Categoria();
        categoria.setId(4);
        when(servicioCategoria.obtenerCategoriaRandom(anyList())).thenReturn(categoria);

        ApiPregunta pregunta = new ApiPregunta();
        when(preguntaService.obtenerPreguntasPorCategoria(10, 4))
                .thenReturn(Collections.singletonList(pregunta));

        controllerCategoria.siguienteCategoria(request);

        verify(preguntaService).obtenerPreguntasPorCategoria(10, 4);
        verify(session).setAttribute("nombreUsuario", "tmansilla7");
    }

    @Test
    public void siguienteCategoriaSaltaCategoriasSinPreguntasHastaEncontrarUnaValida() {
        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(10);
        when(session.getAttribute("puntaje")).thenReturn(0);
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());

        Categoria categoria = new Categoria();
        categoria.setId(7);
        when(servicioCategoria.obtenerCategoriaRandom(anyList())).thenReturn(categoria);

        ApiPregunta pregunta = new ApiPregunta();
        when(preguntaService.obtenerPreguntasPorCategoria(10, 7))
                .thenReturn(Collections.<ApiPregunta>emptyList())
                .thenReturn(Collections.singletonList(pregunta));

        ModelAndView mav = controllerCategoria.siguienteCategoria(request);

        assertThat(mav.getViewName(), equalTo("categoria-pregunta"));
        verify(preguntaService, times(2)).obtenerPreguntasPorCategoria(10, 7);
    }

    @Test
    public void siguienteCategoriaExitosaDevuelveVistaDePreguntaConDatosCorrectos() {
        when(session.getAttribute("cantidadPreguntasTotal")).thenReturn(8);
        when(session.getAttribute("puntaje")).thenReturn(30);
        when(session.getAttribute("nombreUsuario")).thenReturn("tomas");
        when(session.getAttribute("categoriasUsadas")).thenReturn(new ArrayList<Integer>());

        Categoria categoria = new Categoria();
        categoria.setId(9);
        when(servicioCategoria.obtenerCategoriaRandom(anyList())).thenReturn(categoria);

        ApiPregunta pregunta = new ApiPregunta();
        when(preguntaService.obtenerPreguntasPorCategoria(8, 9))
                .thenReturn(Collections.singletonList(pregunta));

        ModelAndView mav = controllerCategoria.siguienteCategoria(request);

        assertThat(mav.getViewName(), equalTo("categoria-pregunta"));
        assertThat((ApiPregunta) mav.getModel().get("pregunta"), is(pregunta));
        assertThat((Integer) mav.getModel().get("preguntasRespondidas"), equalTo(1));
        verify(session).setAttribute("preguntasRespondidas", 0);
        verify(session).setAttribute("indicePregunta", 0);
    }}
    @Test
    public void reiniciarLimpiaTodosLosAtributosDeSesionYRedirige() {
        ModelAndView mav = controllerCategoria.reiniciar(request);

        assertThat(mav.getViewName(), equalTo("redirect:/categoria"));

        verify(session).removeAttribute("categoriasUsadas");
        verify(session).removeAttribute("puntaje");
        verify(session).removeAttribute("preguntaActual");
        verify(session).removeAttribute("categoriaActualId");
        verify(session).removeAttribute("cantidadPreguntasTotal");
        verify(session).removeAttribute("preguntasRespondidas");
        verify(session).removeAttribute("nombreUsuario");
    }
}