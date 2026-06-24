package com.tallerwebi.presentacion.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.servlet.http.HttpSession;
import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.categoriaDia.CategoriaService;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.dominio.pregunta.PreguntaService;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

public class ControllerAdminTest {

    private SessionUtil sessionUtil;
    private PreguntaService preguntaService;
    private CategoriaService categoriaService;
    private RepositoryUsuario repositoryUsuario;

    private HttpSession session;
    private Model model;

    private ControllerAdmin adminController;

    @BeforeEach
    public void init() {

        this.sessionUtil = mock(SessionUtil.class);
        this.preguntaService = mock(PreguntaService.class);
        this.categoriaService = mock(CategoriaService.class);
        this.repositoryUsuario = mock(RepositoryUsuario.class);

        this.session = mock(HttpSession.class);
        this.model = mock(Model.class);

        this.adminController = new ControllerAdmin(
                sessionUtil,
                preguntaService,
                categoriaService,
                repositoryUsuario, null
        );
    }

    // -------------------------------
    // 1. SI NO ES ADMIN → LOGIN
    // -------------------------------

    @Test
    public void debeRedirigirALoginSiNoEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        String vista = adminController.panelAdmin(session, model);

        assertEquals("redirect:/login", vista);

        verify(sessionUtil).verificarAdmin(session);

        verify(model, never()).addAttribute(anyString(), any());
    }

    // -------------------------------
    // 2. NO BORRA MENSAJES SI NO ES ADMIN
    // -------------------------------
    @Test
    public void noDeberiaLimpiarMensajesSiNoEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(false);

        adminController.panelAdmin(session, model);

        verify(session, never()).getAttribute("ok");
        verify(session, never()).removeAttribute("ok");

        verify(session, never()).getAttribute("error");
        verify(session, never()).removeAttribute("error");
    }

    // -------------------------------
    // 3. SI ES ADMIN → CARGA PANEL
    // -------------------------------
    @Test
    public void debeMostrarPanelAdminSiEsAdmin() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(preguntaService.listar()).thenReturn(List.of(new Pregunta(), new Pregunta(), new Pregunta()));
        when(categoriaService.obtenerCategoriaActiva()).thenReturn("Historia");
        when(repositoryUsuario.listarTodos()).thenReturn(List.of(new Usuario(), new Usuario()));

        when(session.getAttribute("ok")).thenReturn(null);
        when(session.getAttribute("error")).thenReturn(null);

        String vista = adminController.panelAdmin(session, model);

        assertEquals("admin/panelAdmin", vista);

        verify(model).addAttribute("mensaje", "Bienvenido administrador");
        verify(model).addAttribute("cantidadPreguntas", 3);
        verify(model).addAttribute("categoriaActual", "Historia");
        verify(model).addAttribute("cantidadUsuarios", 2);
    }

    // -------------------------------
    // 4. MENSAJE OK SE AGREGA
    // -------------------------------
    @Test
    public void debeAgregarMensajeOkSiExiste() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(preguntaService.listar()).thenReturn(List.of());
        when(categoriaService.obtenerCategoriaActiva()).thenReturn("Ciencia");
        when(repositoryUsuario.listarTodos()).thenReturn(List.of());

        when(session.getAttribute("ok")).thenReturn("Operacion exitosa");
        when(session.getAttribute("error")).thenReturn(null);

        adminController.panelAdmin(session, model);

        verify(model).addAttribute("ok", "Operacion exitosa");
        verify(session).removeAttribute("ok");
    }

    // -------------------------------
    // 5. MENSAJE ERROR SE AGREGA
    // -------------------------------
    @Test
    public void debeAgregarMensajeErrorSiExiste() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(preguntaService.listar()).thenReturn(List.of());
        when(categoriaService.obtenerCategoriaActiva()).thenReturn("Ciencia");
        when(repositoryUsuario.listarTodos()).thenReturn(List.of());

        when(session.getAttribute("ok")).thenReturn(null);
        when(session.getAttribute("error")).thenReturn("Error grave");

        adminController.panelAdmin(session, model);

        verify(model).addAttribute("error", "Error grave");
        verify(session).removeAttribute("error");
    }

    // -------------------------------
    // 6. VERIFICA LLAMADA A SERVICIOS
    // -------------------------------
    @Test
    public void debeLlamarServiciosUnaVez() {

        when(sessionUtil.verificarAdmin(session)).thenReturn(true);

        when(preguntaService.listar()).thenReturn(List.of());
        when(categoriaService.obtenerCategoriaActiva()).thenReturn("Geo");
        when(repositoryUsuario.listarTodos()).thenReturn(List.of());

        adminController.panelAdmin(session, model);

        verify(preguntaService, times(1)).listar();
        verify(categoriaService, times(1)).obtenerCategoriaActiva();
        verify(repositoryUsuario, times(1)).listarTodos();
    }
}