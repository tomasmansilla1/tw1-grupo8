package com.tallerwebi.presentacion.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;
import com.tallerwebi.config.SessionUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

public class ControllerAdminTest {
    // obj simulado para no usar bd real
    private SessionUtil sessionUtil;
    private HttpSession session;
    private Model model;

    private ControllerAdmin adminController;

    @BeforeEach
    public void init() {
        // mocks manuales
        this.sessionUtil = mock(SessionUtil.class);
        this.session = mock(HttpSession.class);
        this.model = mock(Model.class);

        // controller real
        this.adminController = new ControllerAdmin(sessionUtil);
    }

    @SuppressWarnings("null")
    @Test
    public void debeRedirigirALoginSiNoEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = adminController.panelAdmin(session, model);
        assertEquals("redirect:/login", vista);

        verify(sessionUtil, times(1)).verificarAdmin(session);
        verify(model, never()).addAttribute(
            anyString(), 
            any(Object.class)
        );
    }

    @Test
    public void debeVerificarAdminUnaVez() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        
        adminController.panelAdmin(session, model);

        verify(sessionUtil, times(1)).verificarAdmin(session);
    }

    @SuppressWarnings("null")
    @Test
    public void debeMostrarPanelAdminSiEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(session.getAttribute("ok")).thenReturn("Operación exitosa");
        when(session.getAttribute("error")).thenReturn(null);

        String vista = adminController.panelAdmin(session, model);

        assertEquals("admin/panelAdmin", vista);

        verify(model).addAttribute(
            "mensaje", 
            "Bienvenido administrador"
        );
        verify(model).addAttribute(
            "ok", 
            "Operación exitosa"
        );

        verify(model, never()).addAttribute(eq("error"), any());
    }

    @Test
    public void deberiaMostrarPanelAunqueNoHayaMensajes() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        when(session.getAttribute("ok")).thenReturn(null);
        when(session.getAttribute("error")).thenReturn(null);

        String vista = adminController.panelAdmin(session, model);

        assertEquals("admin/panelAdmin",vista);
    }

    // MENSAJES DE ERROR Y OK
    @Test
    public void deberiaLimpiarSoloMensajeOkDeSesion() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        when(session.getAttribute("ok")).thenReturn("Operacion correcta");
        when(session.getAttribute("error")).thenReturn(null);

        adminController.panelAdmin(session, model);

        verify(session).removeAttribute("ok");
        verify(session, never()).removeAttribute("error");
    }

    @Test
    public void deberiaLimpiarSoloMensajeErrorDeSesion() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        when(session.getAttribute("ok")).thenReturn(null);
        when(session.getAttribute("error")).thenReturn("Hubo un error");

        adminController.panelAdmin(session, model);

        verify(session, never()).removeAttribute("ok");
        verify(session).removeAttribute("error");
    }

    @Test
    public void deberiaLimpiarAmbosMensajesDeSesion() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(session.getAttribute("ok")).thenReturn("OK");
        when(session.getAttribute("error")).thenReturn("ERROR");

        adminController.panelAdmin(session, model);

        verify(session).removeAttribute("ok");
        verify(session).removeAttribute("error");
    }

    @Test
    public void debeAgregarMensajeErrorAlModel() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);
        
        when(session.getAttribute("ok")).thenReturn(null);
        when(session.getAttribute("error")).thenReturn("Hubo un error");

        adminController.panelAdmin(session, model);

        verify(model).addAttribute(
            "error",
            "Hubo un error"
        );
    }
    @Test
    public void debeAgregarMensajeOkAlModel() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(session.getAttribute("ok"))
            .thenReturn("Operación correcta");

        adminController.panelAdmin(session, model);

        verify(model).addAttribute(
            "ok",
            "Operación correcta"
        );
    }

    @Test
    public void noDeberiaLimpiarMensajesSiNoEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);
        adminController.panelAdmin(session, model);

        verify(session, never()).removeAttribute("ok");
        verify(session, never()).removeAttribute("error");
    }

    // TEST SI ES ADMIN O NO
    @Test
    public void debeRedirigirALoginSiNoEsAdminEnPregunta() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = adminController.gestionarPreguntas(session);
        assertEquals("redirect:/login", vista);
    }
    @Test
    public void debeIrAPaginaPreguntaSiEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = adminController.gestionarPreguntas(session);
        assertEquals("admin/pregunta", vista);
    }
    @Test
    public void debeRedirigirALoginSiNoEsAdminEnCrearPregunta() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = adminController.crearPregunta(session);
        assertEquals("redirect:/login", vista);
    }
    @Test
    public void debeIrACrearPreguntaSiEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = adminController.crearPregunta(session);
        assertEquals("admin/crearPregunta", vista);
    }

    @Test
    public void debeRedirigirALoginSiNoEsAdminEnCategoriaDia() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = adminController.categoriaDia(session);
        assertEquals("redirect:/login", vista);
    }
    @Test
    public void debeIrACategoriaDiaSiEsAdmin() {
        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        String vista = adminController.categoriaDia(session);
        assertEquals("admin/categoriaDia", vista);
    }
}