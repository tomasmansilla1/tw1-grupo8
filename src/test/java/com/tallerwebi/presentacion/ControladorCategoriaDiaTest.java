package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;
import com.tallerwebi.config.SessionUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

public class ControladorCategoriaDiaTest {
    // obj simulado para no usar bd real
    @Mock
    private SessionUtil sessionUtil;
    @Mock
    private HttpSession session;
    @Mock
    private Model model;

    @InjectMocks
    private ControladorCategoriaDia controller;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void noDebeConsultarCategoriaSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        controller.mostrarCategoriaDia( session, model);

        verify(session, never()).getAttribute("categoria_dia");
    }

    @Test
    public void noDebegregarCategoriaAlModelSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        controller.mostrarCategoriaDia(session,model);

        verify(model, never()).addAttribute(
            "categoriaDia",
            null
        );
    }

    // GET
    @Test
    public void debeRedirigirALoginSiNoEsAdminEnGet() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.mostrarCategoriaDia(session,model);

        assertEquals("redirect:/login", vista);
    }
    @Test
    public void debeVerificarAdminUnaVezEnGet() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        controller.mostrarCategoriaDia( session, model);

        verify(sessionUtil, times(1))
            .verificarAdmin(session);
    }

    @Test
    public void debeMostrarVistaSiEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        when(session.getAttribute("categoria_dia")).thenReturn("Deportes");

        String vista = controller.mostrarCategoriaDia( session, model);

        assertEquals("admin/categoriaDia",vista);

        verify(model).addAttribute(
            "categoriaDia",
            "Deportes"
        );
    }
    @Test
    public void debeMostrarVistaSiNoHayaCategoria() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        when(session.getAttribute("categoria_dia")).thenReturn(null);

        String vista = controller.mostrarCategoriaDia(session, model);

        assertEquals("admin/categoriaDia", vista);

        verify(model).addAttribute(
            "categoriaDia",
            null
        );
    }

    // POST
    @Test
    public void debeRedirigirALoginSiNoEsAdminEnPost() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = controller.guardarCategoria("Deportes", session, model);

        assertEquals("redirect:/login", vista);
    }
    @Test
    public void deberiaVerificarAdminUnaSolaVezEnPost() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        controller.guardarCategoria( "Historia", session, model);

        verify(sessionUtil, times(1))
            .verificarAdmin(session);
    }

    @Test
    public void debeMostrarErrorSiCategoriaEsNull() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.guardarCategoria(null,session, model);

        assertEquals("admin/categoriaDia",vista);

        verify(model).addAttribute(
            "error",
            "Seleccionar una categoría"
        );
    }
    @Test
    public void debeMostrarErrorSiCategoriaEsVacia() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.guardarCategoria("",session,model);

        assertEquals("admin/categoriaDia",vista);

        verify(model).addAttribute(
            "error",
            "Seleccionar una categoría"
        );
    }
    @Test
    public void debeMostrarErrorSiCategoriaEstaVacia() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.guardarCategoria( "   ", session, model);

        assertEquals("admin/categoriaDia",vista);

        verify(model).addAttribute(
            "error",
            "Seleccionar una categoría"
        );
    }
    @Test
    public void noDebeGuardarMensajeOkSiCategoriaEsErronea() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        controller.guardarCategoria( "", session,model);

        verify(session, never()).setAttribute(
            "ok",
            "La categoría está actualizada"
        );
    }

    @Test
    public void deberiaGuardarCategoriaYRedirigirAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = controller.guardarCategoria("Entretenimiento",session,model);

        assertEquals("redirect:/admin", vista);

        verify(session).setAttribute(
            "categoria_dia",
            "Entretenimiento"
        );
        verify(session).setAttribute(
            "ok",
            "La categoría está actualizada"
        );
    }
    @Test
    public void deberiaGuardarLaCategoriaCorrecta() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        controller.guardarCategoria( "Ciencia", session, model);

        verify(session).setAttribute(
            "categoria_dia",
            "Ciencia"
        );
    }
    @Test
    public void noDeberiaGuardarCategoriaInvalida() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        controller.guardarCategoria( "", session, model);

        verify(session, never()).setAttribute(
            "categoria_dia",
            ""
        );
    }
}