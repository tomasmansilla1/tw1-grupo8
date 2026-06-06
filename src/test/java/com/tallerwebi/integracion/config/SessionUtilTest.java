package com.tallerwebi.integracion.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tallerwebi.config.SessionUtil;

public class SessionUtilTest {
    private SessionUtil sessionUtil;

    // obj simulado para no usar bd real
    @Mock
    private HttpSession session;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        sessionUtil = new SessionUtil();
    }

    @Test
    public void siExisteUsuarioLaSesionEsValida() {
        when(session.getAttribute("usuario")).thenReturn(new Object());

        boolean resultado = sessionUtil.verificarSesion(session);

        assertTrue(resultado);
    }
    @Test
    public void siNoExisteUsuarioLaSesionNoEsValida() {
        when(session.getAttribute("usuario")).thenReturn(null);

        boolean resultado = sessionUtil.verificarSesion(session);

        assertFalse(resultado);
    }
    @Test
    public void debeConsultarSiHayUsuarioEnSesion() {
        when(session.getAttribute("usuario")).thenReturn(new Object());
        
        sessionUtil.verificarSesion(session);

        verify(session).getAttribute("usuario");
    }

    @Test
    public void noDeberiaConsultarRolSiNoHaySesion() {
        when(session.getAttribute("usuario")).thenReturn(null);

        sessionUtil.verificarAdmin(session);

        verify(session).getAttribute("usuario");
        verify(session, never()).getAttribute("rol");
    }   

    @Test
    public void siNoHaySesionAdminDevuelveFalse() {
        when(session.getAttribute("usuario")).thenReturn(null);

        boolean resultado = sessionUtil.verificarAdmin(session);

        assertFalse(resultado);
    }
    
}