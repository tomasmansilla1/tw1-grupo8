package com.tallerwebi.presentacion.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.admin.AdminIniciador;
import com.tallerwebi.dominio.login.ServiceLogin;
import com.tallerwebi.dominio.usuario.Usuario;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class ControllerLoginTest {
  private MockMvc mockMvc;
  private ServiceLogin servicioLogin;
  private AdminIniciador adminIniciador;
  private ControllerLogin controllerLogin;

  @BeforeEach
  public void init() {
    servicioLogin = mock(ServiceLogin.class);
    controllerLogin = new ControllerLogin(servicioLogin);
    adminIniciador = mock(AdminIniciador.class);

    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix("/WEB-INF/views/");
    viewResolver.setSuffix(".jsp");

    mockMvc = MockMvcBuilders
      .standaloneSetup(controllerLogin)
      .setViewResolvers(viewResolver)
      .build();
    ReflectionTestUtils.setField(
      controllerLogin,
      "adminIniciador",
      adminIniciador
    );
  }

  @Test
  public void debeRedirigirALoginDesdeInicio() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/login"));
  }

  @Test
  public void debeMostrarLogin() throws Exception {
    mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(model().attributeExists("datosLogin"));
  }

  @Test
  public void loginCorrectoDebeRedirigirAHome() throws Exception {
    Usuario usuario = new Usuario();
    usuario.setEmail("dami@unlam.com");
    usuario.setRol("ADMIN");

    when(servicioLogin.consultarUsuario("dami@unlam.com", "1234")).thenReturn(usuario);

    mockMvc.perform(post("/validar-login")
        .param("email", "dami@unlam.com")
        .param("password", "1234"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/home"))
        .andExpect(request().sessionAttribute("usuario", usuario))
        .andExpect(request().sessionAttribute("rol", "ADMIN"));
  }

  @Test
  public void loginIncorrectoDebeVolverALogin() throws Exception {
    when(servicioLogin.consultarUsuario(anyString(), anyString())).thenReturn(null);

    mockMvc.perform(post("/validar-login")
        .param("email", "mal@mail.com")
        .param("password", "1234"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(model().attributeExists("error"));
  }

  @Test
  public void debeIrALoginYCrearAdmin() {

    ModelAndView modelAndView = controllerLogin.irALogin();

    verify(adminIniciador).crearAdmin();

    assertEquals("login", modelAndView.getViewName());
    assertNotNull(modelAndView.getModel().get("datosLogin"));
  }

  @Test
  public void debeIrAHomeSiHayUsuarioEnSesion() {

    HttpSession session = mock(HttpSession.class);
    Usuario usuario = new Usuario();

    when(session.getAttribute("usuario")).thenReturn(usuario);

    ModelAndView modelAndView = controllerLogin.irAHome(session);

    assertEquals(
      "home",
      modelAndView.getViewName()
    );
  }

  @Test
  public void debeRedirigirALoginSiNoHayUsuarioEnSesion() {

    HttpSession session = mock(HttpSession.class);

    when(session.getAttribute("usuario")).thenReturn(null);

    ModelAndView modelAndView = controllerLogin.irAHome(session);

    assertEquals(
      "redirect:/login",
      modelAndView.getViewName()
    );
  }

  @Test
  public void debeRedirigirALoginPorElnicio() {

    ModelAndView modelAndView = controllerLogin.inicio();

    assertEquals(
      "redirect:/login",
      modelAndView.getViewName()
    );
  }

  @Test
  public void debeInvalidarSesionYRedirigirALogin() {

    HttpSession session = mock(HttpSession.class);

    String vista = controllerLogin.logout(session);

    verify(session).invalidate();

    assertEquals(
      "redirect:/login",
      vista
    );
  }
}