package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.Pregunta;
import com.tallerwebi.dominio.PreguntasService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

public class ControladorPreguntaTest {

    // obj simulado para no usar bd real
    @Mock
    private PreguntasService preguntaService;
    @Mock
    private SessionUtil sessionUtil;
    @Mock
    private HttpSession session;
    @Mock
    private Model model;

    @InjectMocks
    private ControladorPregunta controller;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void debeAgregarMensajesAlModel() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        when(session.getAttribute("ok")).thenReturn("todo bien");
        when(session.getAttribute("error")).thenReturn("hubo error");

        controller.listarPreguntas( session, model);

        verify(model).addAttribute(
            "ok",
            "todo bien"
        );
        verify(model).addAttribute(
            "error",
             "hubo error"
        );
    }
    @Test
    public void noDebeLimpiarMensajesSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        controller.listarPreguntas(session, model);

        verify(session, never()).removeAttribute("ok");
        verify(session, never()).removeAttribute("error");
    }   
    
    // MOSTRAR FORM
    @Test
    public void debeMostrarFormSiEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.mostrarFormulario(session);

        assertEquals("admin/crearPregunta", vista);
    }
    @Test
    public void debeRedirigirLoginSiNoEsAdminEnForm() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.mostrarFormulario(session);

        assertEquals("redirect:/login", vista);
    }

    //GUARDAR PREGUNTA
    @Test
    public void debeGuardarPregunta() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();
        String vista = controller.guardarPregunta( pregunta,session);

        assertEquals("redirect:/admin/preguntas", vista);
        verify(preguntaService).guardar(pregunta);

        verify(session).setAttribute(
            "ok",
            "Pregunta creada correctamente"
        );
    }
    @Test
    public void noDebeGuardarSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        Pregunta pregunta = new Pregunta();
        String vista = controller.guardarPregunta(pregunta, session);

        assertEquals("redirect:/login",vista);
        verify(preguntaService, never()).guardar(pregunta);
    }

    // LISTAR PREGUNTA
    @Test
    public void noDebeListarPreguntasSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.listarPreguntas(session, model);

        assertEquals("redirect:/login", vista);
        verify(preguntaService, never()).listar();
    }
    @Test
    public void debeListarPreguntas() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        List<Pregunta> preguntas = new ArrayList<>();
        when(preguntaService.listar()).thenReturn(preguntas);

        String vista = controller.listarPreguntas(session, model);

        assertEquals("admin/preguntas",vista);
        verify(model).addAttribute("preguntas", preguntas);
    }

    @Test
    public void debeLimpiarMensajesDeSesion() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        controller.listarPreguntas(session,model);

        verify(session).removeAttribute("ok");
        verify(session).removeAttribute("error");
    }

    // EDITAR PREGUNTA
    @Test
    public void debeMostrarEditarPregunta() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();
        when(preguntaService.obtenerPorId(1L)).thenReturn(pregunta);

        String vista = controller.editarPregunta(1L, session, model);

        assertEquals("admin/editarPregunta",vista);
        verify(model).addAttribute("pregunta", pregunta);
    }

    @Test
    public void debeRedirigirSiIdEsNull() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.editarPregunta( null, session, model);

        assertEquals("redirect:/admin/preguntas",vista);

        verify(session).setAttribute(
            "error",
            "ID erroneo"
        );
    }
    @Test
    public void noDeberiaBuscarPreguntaSiIdEsNull() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        controller.editarPregunta(null,session, model);

        verify(preguntaService, never()).obtenerPorId(anyLong());
    }

    @Test
    public void debeRedirigirSiPreguntaNoExiste() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        when(preguntaService.obtenerPorId(1L)).thenReturn(null);

        String vista = controller.editarPregunta(1L, session, model);

        assertEquals("redirect:/admin/preguntas",vista);
        verify(session).setAttribute(
            "error",
            "Pregunta no encontrada"
        );
    }

    // ACTUALIZAR PREGUNTA
    @Test
    public void noDebeActualizarPreguntaSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        Pregunta pregunta = new Pregunta();
        String vista = controller.actualizarPregunta(pregunta, session);

        assertEquals("redirect:/login",vista);
        verify(preguntaService, never()).guardar(pregunta);
    }
    @Test
    public void debeActualizarPregunta() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();

        String vista = controller.actualizarPregunta( pregunta, session);

        assertEquals("redirect:/admin/preguntas",vista);
        verify(preguntaService).guardar(pregunta);
    }

    // ELIMINAR PREGUNTA
    @Test
    public void noDebeEliminarSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.eliminarPregunta(1L,session);

        assertEquals("redirect:/login", vista);
        verify(preguntaService, never()).eliminar(1L);
    }

    @Test
    public void debeEliminarPregunta() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.eliminarPregunta(1L, session);

        assertEquals("redirect:/admin/preguntas",vista);
        verify(preguntaService).eliminar(1L);

        verify(session).setAttribute(
            "ok",
            "Pregunta eliminada correctamente"
        );
    }
    @Test
    public void noDebeEliminarPreguntaSiIdEsNull() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        controller.eliminarPregunta(null, session);

        verify(preguntaService, never()).eliminar(anyLong());
    }

    @Test
    public void debeRedirigirSiIdEliminarEsNull() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.eliminarPregunta(null, session);

        assertEquals("redirect:/admin/preguntas",vista);
        verify(session).setAttribute("error","ID inválido");
    }
}