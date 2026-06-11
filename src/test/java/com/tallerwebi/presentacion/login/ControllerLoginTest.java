package com.tallerwebi.presentacion.login;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.login.ServiceLogin;
import com.tallerwebi.dominio.usuario.Usuario;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class ControllerLoginTest {

  private MockMvc mockMvc;
  private ServiceLogin servicioLogin;
  private ControllerLogin controllerLogin;

  @BeforeEach
  public void init() {
    servicioLogin = mock(ServiceLogin.class);
    controllerLogin = new ControllerLogin(servicioLogin);

    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

    viewResolver.setPrefix("/WEB-INF/views/");
    viewResolver.setSuffix(".jsp");

    mockMvc = MockMvcBuilders
      .standaloneSetup(controllerLogin)
      .setViewResolvers(viewResolver)
      .build();
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

    when(servicioLogin.consultarUsuario(
        "dami@unlam.com",
        "1234"
    )).thenReturn(usuario);

    mockMvc.perform(post("/validar-login")
            .param("email", "dami@unlam.com")
            .param("password", "1234"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/home"))
        .andExpect(request()
            .sessionAttribute("usuario", usuario))
        .andExpect(request()
            .sessionAttribute("rol", "ADMIN"));
  }

  @Test
  public void loginIncorrectoDebeVolverALogin() throws Exception {

    when(servicioLogin.consultarUsuario(
        anyString(),
        anyString()
    )).thenReturn(null);

    mockMvc.perform(post("/validar-login")
            .param("email", "mal@mail.com")
            .param("password", "1234"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(model().attributeExists("error"));
  }

  @Test
  public void debeMostrarHome() throws Exception {

    mockMvc.perform(get("/home"))
        .andExpect(status().isOk())
        .andExpect(view().name("home"));
  }
}