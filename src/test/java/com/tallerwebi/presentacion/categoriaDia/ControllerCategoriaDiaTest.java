package com.tallerwebi.presentacion.categoriaDia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.categoriaDia.CategoriaService;

import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

public class ControllerCategoriaDiaTest {
    private SessionUtil sessionUtil;
    private CategoriaService categoriaService;

    private HttpSession session;
    private Model model;

    private ControllerCategoriaDia controller;

    @BeforeEach
    public void init() {

        this.sessionUtil = mock(SessionUtil.class);
        this.categoriaService = mock(CategoriaService.class);

        this.session = mock(HttpSession.class);
        this.model = mock(Model.class);

        this.controller = new ControllerCategoriaDia(sessionUtil, categoriaService);
    }

    // --------------------------------------
    // 1. SI NO ES ADMIN → REDIRECCION GET
    // --------------------------------------
    @Test
    public void debeRedirigirALoginSiNoEsAdminEnGet() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.mostrarCategoriaDia(session, model);

        assertEquals("redirect:/login", vista);

        verifyNoInteractions(categoriaService);
        verify(session, never()).removeAttribute(anyString());
    }

    // --------------------------------------
    // 2. SI ES ADMIN → CARGA DATOS GET
    // --------------------------------------
    @Test
    public void debeMostrarVistaCategoriaSiEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(categoriaService.obtenerCategoriaActiva()).thenReturn("Historia");
        when(categoriaService.obtenerHistorial()).thenReturn(List.of());

        when(session.getAttribute("ok")).thenReturn("ok msg");
        when(session.getAttribute("error")).thenReturn("error msg");

        String vista = controller.mostrarCategoriaDia(session, model);

        assertEquals("admin/categoriaDia", vista);

        verify(model).addAttribute("categoriaDia", "Historia");
        verify(model).addAttribute("historial", List.of());
        verify(model).addAttribute("ok", "ok msg");
        verify(model).addAttribute("error", "error msg");

        verify(session).removeAttribute("ok");
        verify(session).removeAttribute("error");
    }

    // --------------------------------------
    // 3. POST: SI NO ES ADMIN → LOGIN
    // --------------------------------------
    @Test
    public void debeRedirigirALoginSiNoEsAdminEnPost() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.guardarCategoria("Deportes", session);

        assertEquals("redirect:/login", vista);

        verifyNoInteractions(categoriaService);
    }

    // --------------------------------------
    // 4. POST: CATEGORIA VACIA → ERROR SESSION
    // --------------------------------------
    @Test
    public void debeGuardarErrorSiCategoriaVacia() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.guardarCategoria("", session);

        assertEquals("redirect:/admin/categoriaDia", vista);

        verify(session).setAttribute(
                eq("error"),
                eq("Debe seleccionar una categoría")
        );

        verify(categoriaService, never()).guardarNuevaCategoria(anyString());
    }

    // --------------------------------------
    // 5. POST: CATEGORIA VALIDA → GUARDA
    // --------------------------------------
    @Test
    public void debeGuardarCategoriaCorrectamente() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.guardarCategoria("Deportes", session);

        assertEquals("redirect:/admin/categoriaDia", vista);

        verify(categoriaService).guardarNuevaCategoria("Deportes");

        verify(session).setAttribute(
                "ok",
                "Categoría actualizada correctamente"
        );
    }

    // --------------------------------------
    // 6. NO DEBE LLAMAR SERVICIO SI ERROR
    // --------------------------------------
    @Test
    public void noDebeGuardarSiCategoriaInvalida() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        controller.guardarCategoria("   ", session);

        verify(categoriaService, never()).guardarNuevaCategoria(anyString());
    }
}