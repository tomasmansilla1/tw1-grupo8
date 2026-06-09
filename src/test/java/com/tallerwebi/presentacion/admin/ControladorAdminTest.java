package com.tallerwebi.presentacion.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.config.SessionUtil;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

public class ControladorAdminTest {

  // obj simulado para no usar bd real
  @Mock
  private SessionUtil sessionUtil;

  @Mock
  private HttpSession session;

  @Mock
  private Model model;

  @InjectMocks
  private ControladorAdmin adminController;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @SuppressWarnings("null")
  @Test
  public void debeRedirigirALoginSiNoEsAdmin() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(false);

    String vista = adminController.panelAdmin(session, model);
    assertEquals("redirect:/login", vista);

    verify(sessionUtil, times(1)).verificarAdmin(session);
    verify(model, never()).addAttribute(anyString(), any());
  }

  @Test
  public void debeVerificarAdminUnaVez() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    adminController.panelAdmin(session, model);

    verify(sessionUtil, times(1)).verificarAdmin(session);
  }

  @Test
  public void debeMostrarPanelAdminSiEsAdmin() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    when(session.getAttribute("ok")).thenReturn("Operación exitosa");
    when(session.getAttribute("error")).thenReturn(null);

    String vista = adminController.panelAdmin(session, model);
    assertEquals("admin/panelAdmin", vista);

    verify(model).addAttribute("mensaje", "Bienvenido administrador");
    verify(model).addAttribute("ok", "Operación exitosa");
    verify(model).addAttribute("error", null);
  }

  @Test
  public void deberiaMostrarPanelAunqueNoHayaMensajes() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);
    when(session.getAttribute("ok")).thenReturn(null);
    when(session.getAttribute("error")).thenReturn(null);

    String vista = adminController.panelAdmin(session, model);

    assertEquals("admin/panelAdmin", vista);
  }

  @Test
  public void deberiaLimpiarMensajesDeSesion() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);
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

    verify(model).addAttribute("error", "Hubo un error");
  }

  @Test
  public void debeAgregarMensajeOkAlModel() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    when(session.getAttribute("ok")).thenReturn("Operación correcta");

    adminController.panelAdmin(session, model);

    verify(model).addAttribute("ok", "Operación correcta");
  }

  @Test
  public void noDeberiaLimpiarMensajesSiNoEsAdmin() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(false);
    adminController.panelAdmin(session, model);

    verify(session, never()).removeAttribute("ok");
    verify(session, never()).removeAttribute("error");
  }
}
