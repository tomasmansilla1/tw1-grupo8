package com.tallerwebi.presentacion.pregunta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.dominio.pregunta.PreguntaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ControladorPreguntaTest {

    // obj simulado para no usar bd real
    private PreguntaService preguntaService;
    private SessionUtil sessionUtil;
    private HttpSession session;
    private Model model;
    private RedirectAttributes redirectAttributes;

    private ControllerPregunta controller;

    @BeforeEach
    public void init() {
        // mocks manuales
        this.preguntaService = mock(PreguntaService.class);
        this.sessionUtil = mock(SessionUtil.class);
        this.session = mock(HttpSession.class);
        this.model = mock(Model.class);
        this.redirectAttributes = mock(RedirectAttributes.class);

        // controller real
        this.controller = new ControllerPregunta(preguntaService, sessionUtil);
    }


  @Test
  public void debeAgregarMensajesAlModel() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);
    when(session.getAttribute("ok")).thenReturn("todo bien");
    when(session.getAttribute("error")).thenReturn("hubo error");

    controller.listarPreguntas(session, model);

    verify(model).addAttribute("ok", "todo bien");
    verify(model).addAttribute("error", "hubo error");
  }

  @Test
  public void debeRedirigirALoginSiNoEsAdminEnEditar() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(false);

    String vista = controller.editarPregunta(1L, session, model);
    assertEquals("redirect:/login", vista);
  }
  @Test
  public void debeContinuarSiEsAdminEnEditar() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);
    when(preguntaService.obtenerPorId(1L)).thenReturn(new Pregunta());

    String vista = controller.editarPregunta(1L, session, model);
    assertEquals("admin/editarPregunta", vista);
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
    String vista = controller.mostrarFormulario(session, model);

    assertEquals("admin/crearPregunta", vista);
    verify(model).addAttribute(eq("pregunta"), any(Pregunta.class));
  }

  @Test
  public void debeRedirigirLoginSiNoEsAdminEnForm() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(false);

    String vista = controller.mostrarFormulario(session, model);

    assertEquals("redirect:/login", vista);
  }

  //GUARDAR PREGUNTA
  @Test
  public void debeGuardarPregunta() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    Pregunta pregunta = new Pregunta();
    String vista = controller.guardarPregunta(pregunta, session);

    assertEquals("redirect:/admin/preguntas", vista);
    verify(preguntaService).guardar(pregunta);

    verify(session).setAttribute("ok", "Pregunta creada correctamente");
  }

  @Test
  public void noDebeGuardarSiNoEsAdmin() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(false);

    Pregunta pregunta = new Pregunta();
    String vista = controller.guardarPregunta(pregunta, session);

    assertEquals("redirect:/login", vista);
    verify(preguntaService, never()).guardar(pregunta);
  }

  // LISTAR PREGUNTA
  @Test
  public void debeListarPreguntasSiEsAdmin() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);
    when(preguntaService.listar()).thenReturn(new ArrayList<>());

    String vista = controller.listarPreguntas(session, model);

    assertEquals("admin/pregunta", vista);
    verify(preguntaService).listar();
    verify(model).addAttribute(eq("pregunta"), anyList());
  }

  @Test
  public void noDebeListarPreguntasSiNoEsAdmin() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(false);
    String vista = controller.listarPreguntas(session, model);

    assertEquals("redirect:/login",vista);
    verify(preguntaService, never()).listar();
  }

  @Test
  public void debeListarPreguntas() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);
    List<Pregunta> preguntas = new ArrayList<>();

    when(preguntaService.listar()).thenReturn(preguntas);
    String vista = controller.listarPreguntas(session,model);

    assertEquals("admin/pregunta",vista);
    verify(model).addAttribute("pregunta",preguntas);
  }

  @Test
  public void debeLimpiarMensajesDeSesion() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    controller.listarPreguntas(session, model);

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
    assertEquals("admin/editarPregunta", vista);

    verify(model).addAttribute("pregunta", pregunta);
  }

  @Test
  public void debeRedirigirSiIdEsNull() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    String vista = controller.editarPregunta(null, session, model);

    assertEquals("redirect:/admin/preguntas", vista);

    verify(session).setAttribute("error", "ID erroneo");
  }

  @Test
  public void noDeberiaBuscarPreguntaSiIdEsNull() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    controller.editarPregunta(null, session, model);
    verify(preguntaService, never()).obtenerPorId(anyLong());
  }

  @Test
  public void debeRedirigirSiPreguntaNoExiste() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);
    when(preguntaService.obtenerPorId(1L)).thenReturn(null);

    String vista = controller.editarPregunta(1L, session, model);

    assertEquals("redirect:/admin/preguntas", vista);
    verify(session).setAttribute("error", "Pregunta no encontrada");
  }

  // ACTUALIZAR PREGUNTA
  @Test
  public void noDebeActualizarPreguntaSiNoEsAdmin() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(false);

    Pregunta pregunta = new Pregunta();
    String vista = controller.actualizarPregunta(pregunta, session);

    assertEquals("redirect:/login", vista);
    verify(preguntaService, never()).guardar(pregunta);
  }

  @Test
  public void debeActualizarPregunta() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    Pregunta pregunta = new Pregunta();

    String vista = controller.actualizarPregunta(pregunta, session);

    assertEquals("redirect:/admin/preguntas", vista);
    verify(preguntaService).guardar(pregunta);
  }

  // ELIMINAR PREGUNTA
  @Test
  public void noDebeEliminarSiNoEsAdmin() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(false);

    String vista = controller.eliminarPregunta(1L, session, redirectAttributes);

    assertEquals("redirect:/login", vista);
    verify(preguntaService, never()).eliminar(1L);
  }

  @Test
  public void debeEliminarPregunta() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);
    // Existe la pregunta?
    when(preguntaService.obtenerPorId(1L)).thenReturn(new Pregunta()); 

    String vista = controller.eliminarPregunta(1L, session, redirectAttributes);

    assertEquals("redirect:/admin/pregunta",vista);
    verify(preguntaService).eliminar(1L);

    verify(redirectAttributes).addFlashAttribute(
      "ok", 
      "Pregunta eliminada correctamente");
    
  }

  @Test
  public void noDebeEliminarPreguntaSiIdEsNull() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    String vista = controller.eliminarPregunta(1L,session,redirectAttributes);

    assertEquals("redirect:/login", vista);

    verify(preguntaService, never()).eliminar(1L);
  }

  @Test
  public void debeRedirigirSiIdEliminarEsNull() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);

    String vista = controller.eliminarPregunta(null, session, redirectAttributes);

    assertEquals("redirect:/admin/pregunta",vista);
    verify(redirectAttributes).addFlashAttribute("error", "ID inválido");
  } 

  @Test
  public void debeRedirigirSiPreguntaAEliminarNoExiste() {
    when(sessionUtil.verificarAdmin(session)).thenReturn(true);
    when(preguntaService.obtenerPorId(1L)).thenReturn(null); // No existe

    String vista = controller.eliminarPregunta(1L, session, redirectAttributes);

    assertEquals("redirect:/admin/pregunta", vista);
    verify(redirectAttributes).addFlashAttribute("error", "La pregunta no existe");
    verify(preguntaService, never()).eliminar(1L);
  }
}