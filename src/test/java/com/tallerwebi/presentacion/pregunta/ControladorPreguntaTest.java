package com.tallerwebi.presentacion.pregunta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.categoriaDia.CategoriaEnum;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.dominio.pregunta.PreguntaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ControladorPreguntaTest {

    private PreguntaService preguntaService;
    private SessionUtil sessionUtil;

    private HttpSession session;
    private Model model;
    private RedirectAttributes redirectAttributes;

    private ControllerPregunta controller;

    @BeforeEach
    public void init() {

        this.preguntaService = mock(PreguntaService.class);
        this.sessionUtil = mock(SessionUtil.class);

        this.session = mock(HttpSession.class);
        this.model = mock(Model.class);
        this.redirectAttributes = mock(RedirectAttributes.class);

        this.controller = new ControllerPregunta(preguntaService, sessionUtil);

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
    }

    // --------------------------------------------------
    // 1. FORMULARIO
    // --------------------------------------------------
    @Test
    public void debeRedirigirLoginSiNoEsAdminEnFormulario() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.mostrarFormulario(session, model);

        assertEquals("redirect:/login", vista);
    }

    @Test
    public void debeMostrarFormularioSiEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.mostrarFormulario(session, model);

        assertEquals("admin/crearPregunta", vista);

        verify(model).addAttribute(eq("pregunta"), any(Pregunta.class));
    }

    // --------------------------------------------------
    // 2. LISTAR
    // --------------------------------------------------

    @Test
    public void debeAgregarMensajesAlModel() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(preguntaService.listar()).thenReturn(List.of(new Pregunta()));

        when(session.getAttribute("ok")).thenReturn("ok");
        when(session.getAttribute("error")).thenReturn("error");

        controller.listarPreguntas(session, model);

        verify(model).addAttribute("ok", "ok");
        verify(model).addAttribute("error", "error");
        verify(model).addAttribute(eq("preguntas"), any());
    }

    @Test
    public void noDebeListarSiNoEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.listarPreguntas(session, model);

        assertEquals("redirect:/login", vista);

        verify(preguntaService, never()).listar();
    }

    // --------------------------------------------------
    // 3. EDITAR GET
    // --------------------------------------------------
    @Test
    public void debeRedirigirLoginSiNoEsAdminEnEditar() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.editarPregunta(1L, session, model);

        assertEquals("redirect:/login", vista);
    }

    @Test
    public void debeMostrarEditarPregunta() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();
        when(preguntaService.obtenerPorId(1L)).thenReturn(pregunta);

        String vista = controller.editarPregunta(1L, session, model);

        assertEquals("admin/editarPregunta", vista);

        verify(model).addAttribute("pregunta", pregunta);
    }

    @Test
    public void debeRedirigirSiPreguntaNoExiste() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        when(preguntaService.obtenerPorId(1L)).thenReturn(null);

        String vista = controller.editarPregunta(1L, session, model);

        assertEquals("redirect:/admin/preguntas", vista);

        verify(session).setAttribute("error", "Pregunta no encontrada");
    }

    // --------------------------------------------------
    // 4. GUARDAR
    // --------------------------------------------------
    @Test
    public void noDebeGuardarSiNoEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        Pregunta pregunta = new Pregunta();

        String vista = controller.guardarPregunta(pregunta, session);

        assertEquals("redirect:/login", vista);

        verify(preguntaService, never()).guardar(any());
    }

    @Test
    public void debeGuardarPreguntaCorrectamente() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();
        pregunta.setConsigna("test");
        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");
        pregunta.setCategoria("Historia");
        pregunta.setCorrecta("A");

        String vista = controller.guardarPregunta(pregunta, session);

        assertEquals("redirect:/admin/preguntas", vista);

        verify(preguntaService).guardar(pregunta);
        verify(session).setAttribute("ok", "Pregunta creada correctamente");
    }

    // --------------------------------------------------
    // 5. EDITAR POST
    // --------------------------------------------------
    @Test
    public void debeActualizarPregunta() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();

        String vista = controller.actualizarPregunta(pregunta, session);

        assertEquals("redirect:/admin/preguntas", vista);

        verify(preguntaService).guardar(pregunta);
        verify(session).setAttribute("ok", "Pregunta actualizada");
    }

    // --------------------------------------------------
    // 6. ELIMINAR
    // --------------------------------------------------
    @Test
    public void noDebeEliminarSiNoEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.eliminarPregunta(1L, session, redirectAttributes);

        assertEquals("redirect:/login", vista);

        verify(preguntaService, never()).eliminar(anyLong());
    }

    // =======================
// GUARDAR PREGUNTA (POST)
// =======================

    @Test
    public void noDebeGuardarPreguntaSiOpcionesInvalidas() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();
        pregunta.setOpcionA("");
        pregunta.setOpcionB("");
        pregunta.setOpcionC("");
        pregunta.setOpcionD("");

        String vista = controller.guardarPregunta(pregunta, session);

        assertEquals("redirect:/admin/mostrarCrearPregunta", vista);
        verify(preguntaService, never()).guardar(any());
        verify(session).setAttribute(eq("error"), anyString());
    }

    @Test
    public void noDebeGuardarPreguntaSiConsignaVacia() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();
        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");
        pregunta.setConsigna("");

        String vista = controller.guardarPregunta(pregunta, session);

        assertEquals("redirect:/admin/mostrarCrearPregunta", vista);
        verify(preguntaService, never()).guardar(any());
    }

    @Test
    public void noDebeGuardarPreguntaSiCategoriaInvalida() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();
        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");
        pregunta.setConsigna("Pregunta?");
        pregunta.setCategoria("INVALIDA");

        String vista = controller.guardarPregunta(pregunta, session);

        assertEquals("redirect:/admin/mostrarCrearPregunta", vista);
        verify(preguntaService, never()).guardar(any());
    }

// =======================
// ELIMINAR PREGUNTA
// =======================


    @Test
    public void debeRedirigirConErrorSiIdNullEnEliminar() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.eliminarPregunta(null, session, redirectAttributes);

        assertEquals("redirect:/admin/preguntas", vista);
        verify(redirectAttributes).addFlashAttribute(eq("error"), anyString());
        verify(preguntaService, never()).eliminar(any());
    }
    
    @Test
    public void debeRedirigirSiPreguntaNoExisteEnEliminar() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        when(preguntaService.obtenerPorId(1L)).thenReturn(null);

        String vista = controller.eliminarPregunta(1L, session, redirectAttributes);

        assertEquals("redirect:/admin/preguntas", vista);
        verify(redirectAttributes).addFlashAttribute(
            eq("error"),
            eq("La pregunta no existe")
        );
        verify(preguntaService, never()).eliminar(anyLong());
    }

    @Test
    public void debeEliminarPreguntaCorrectamente() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();
        when(preguntaService.obtenerPorId(1L)).thenReturn(pregunta);

        String vista = controller.eliminarPregunta(1L, session, redirectAttributes);

        assertEquals("redirect:/admin/preguntas", vista);
        verify(preguntaService).eliminar(1L);
        verify(redirectAttributes).addFlashAttribute(
            eq("ok"),
            eq("Pregunta eliminada correctamente")
        );
    }

// =======================
// LISTAR PREGUNTAS
// =======================

    @Test
    public void debeListarPreguntasCorrectamente() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(preguntaService.listar()).thenReturn(new ArrayList<>());

        String vista = controller.listarPreguntas(session, model);

        assertEquals("redirect:/admin/preguntas", vista);
        verify(model).addAttribute(eq("preguntas"), any());
    }

    @Test
    public void debeNoListarSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.listarPreguntas(session, model);

        assertEquals("redirect:/login", vista);
        verify(preguntaService, never()).listar();
    }

    @Test
    void debeRechazarRespuestaIncorrecta() {

        Pregunta pregunta = new Pregunta();

        pregunta.setConsigna("Pregunta");
        pregunta.setCategoria("HISTORIA");
        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");

        pregunta.setCorrecta("X");

        String vista =
        controller.guardarPregunta(
            pregunta,
            session
        );

        verify(session).setAttribute(
            "error",
            "La respuesta correcta debe ser A, B, C o D"
        );

        assertEquals(
            "redirect:/admin/mostrarCrearPregunta",
            vista
        );
    }

    @Test
    void debeRechazarCategoriaInvalida() {

        Pregunta pregunta = new Pregunta();

        pregunta.setConsigna("Pregunta");
        pregunta.setCategoria("CUALQUIERA");

        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");

        pregunta.setCorrecta("A");

        String vista = controller.guardarPregunta(
            pregunta,
            session
        );

        verify(session).setAttribute(
            "error",
            "Categoría invalida"
        );

        assertEquals(
            "redirect:/admin/mostrarCrearPregunta",
            vista
        );
    }
    @Test
    void debeGuardarPreguntaValida() {

        Pregunta pregunta = new Pregunta();

        pregunta.setConsigna("Pregunta");

        pregunta.setCategoria(
            CategoriaEnum.HISTORIA.name()
        );

        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");

        pregunta.setCorrecta("A");

        String vista = controller.guardarPregunta(
            pregunta,
            session
        );

        verify(preguntaService).guardar(pregunta);

        assertEquals(
            "redirect:/admin/preguntas",
            vista
        );
    }

    @Test
    public void debeRedirigirALoginSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        Pregunta pregunta = new Pregunta();
        String vista = controller.guardarPregunta(pregunta, session);

        assertEquals("redirect:/login", vista);
    }

    @Test
    public void debeMostrarErrorSiFaltanOpciones() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();

        pregunta.setOpcionA("");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");

        String vista = controller.guardarPregunta(pregunta, session);

        verify(session).setAttribute(
            "error",
            "Todas las opciones son obligatorias"
        );
        assertEquals(
            "redirect:/admin/mostrarCrearPregunta",
            vista
        )  ;
    }

    @Test
    public void debeMostrarErrorSiLaConsignaEsVacia() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        Pregunta pregunta = new Pregunta();

        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");
        pregunta.setConsigna("");

        String vista = controller.guardarPregunta(pregunta, session);

        verify(session).setAttribute(
            "error",
            "La consigna es obligatoria"
        );

        assertEquals(
            "redirect:/admin/mostrarCrearPregunta",
            vista
        );
    }

    @Test
    public void debeMostrarErrorSiLaCategoriaEsVacia() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();

        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");
        pregunta.setConsigna("Pregunta");
        pregunta.setCategoria("");

        String vista = controller.guardarPregunta(pregunta, session);

        verify(session).setAttribute(
            "error",
            "La categoría es obligatoria"
        );
        assertEquals(
            "redirect:/admin/mostrarCrearPregunta",
            vista
        );
    }

    @Test
    public void debeMostrarErrorSiNoHayRespuestaCorrecta() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();

        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");

        pregunta.setConsigna("Pregunta");
        pregunta.setCategoria("HISTORIA");

        pregunta.setCorrecta("");

        String vista = controller.guardarPregunta(pregunta, session);

        verify(session).setAttribute(
            "error",
            "Debe indicar la respuesta correcta"
        );
        assertEquals(
            "redirect:/admin/mostrarCrearPregunta",
            vista
        );
    }

    @Test
    public void debeMostrarErrorSiLaRespuestaNoEsABCD() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();

        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");

        pregunta.setConsigna("Pregunta");
        pregunta.setCategoria("HISTORIA");

        pregunta.setCorrecta("X");

        String vista = controller.guardarPregunta(pregunta, session);

        verify(session).setAttribute(
            "error",
            "La respuesta correcta debe ser A, B, C o D"
        );
        assertEquals(
            "redirect:/admin/mostrarCrearPregunta",
            vista
        );
    }

    @Test
    public void debeMostrarErrorSiLaCategoriaNoExiste() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Pregunta pregunta = new Pregunta();

        pregunta.setOpcionA("A");
        pregunta.setOpcionB("B");
        pregunta.setOpcionC("C");
        pregunta.setOpcionD("D");

        pregunta.setConsigna("Pregunta");
        pregunta.setCategoria("CUALQUIERA");

        pregunta.setCorrecta("A");

        String vista = controller.guardarPregunta(pregunta, session);

        verify(session).setAttribute(
            "error",
            "Categoría invalida"
        );
        assertEquals(
            "redirect:/admin/mostrarCrearPregunta",
            vista
        );
    }

    @Test
    public void debeMostrarErrorSiIdEsNull() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        Model model = mock(Model.class);

        String vista = controller.editarPregunta(
            null,
            session,
            model
        );

        verify(session).setAttribute(
            "error",
            "ID erroneo"
        );

        assertEquals(
            "redirect:/admin/preguntas",
            vista
        );
    }
}