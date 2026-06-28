package com.tallerwebi.dominio.login;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ServiceLoginTest {

  private ServiceLogin servicioLogin;
  private RepositoryUsuario repositorioUsuarioMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositoryUsuario.class);
    this.servicioLogin = new ServiceLoginImpl(this.repositorioUsuarioMock);
  }

  @Test
  public void consultarUsuarioDeberiaLlamarAlRepositorio() {
    // preparacion
    String email = "test@test.com";
    String password = "password";
    Usuario usuarioEsperado = new Usuario();
    when(this.repositorioUsuarioMock.buscarUsuario(email, password)).thenReturn(usuarioEsperado);

    // ejecucion
    Usuario usuarioObtenido = this.servicioLogin.consultarUsuario(email, password);

    // validacion
    assertThat(usuarioObtenido, equalTo(usuarioEsperado));
    verify(this.repositorioUsuarioMock, times(1)).buscarUsuario(email, password);
  }

  @Test
  public void registrarUsuarioSiNoExisteDeberiaGuardarlo() throws UsuarioExistente {
    // preparacion
    Usuario usuario = new Usuario();
    usuario.setEmail("nuevo@test.com");
    usuario.setPassword("123");
    when(this.repositorioUsuarioMock.buscarUsuario(usuario.getEmail(), usuario.getPassword()))
      .thenReturn(null);

    // ejecucion
    this.servicioLogin.registrar(usuario);

    // validacion
    verify(this.repositorioUsuarioMock, times(1)).guardar(usuario);
  }

  @Test
  public void registrarUsuarioSiExisteDeberiaLanzarExcepcion() {
    // preparacion
    Usuario usuario = new Usuario();
    usuario.setEmail("existe@test.com");
    usuario.setPassword("123");
    when(this.repositorioUsuarioMock.buscarUsuario(usuario.getEmail(), usuario.getPassword()))
      .thenReturn(new Usuario());

    // ejecucion y validacion
    assertThrows(UsuarioExistente.class, () -> this.servicioLogin.registrar(usuario));
    verify(this.repositorioUsuarioMock, times(0)).guardar(usuario);
  }

  //DATOS LOGIN
  @Test
  void deberiaCrearObjetoConConstructorVacio() {
    DatosLogin datos = new DatosLogin();

    assertNull(datos.getEmail());
    assertNull(datos.getPassword());
  }
  @Test
  void deberiaCrearObjetoConConstructorConParametros() {
    DatosLogin datos = new DatosLogin("test@mail.com", "Pass123!");

    assertEquals("test@mail.com", datos.getEmail());
    assertEquals("Pass123!", datos.getPassword());
  }
  @Test
  void deberiaSetearEmailCorrectamente() {
    DatosLogin datos = new DatosLogin();

    datos.setEmail("mail@test.com");
    assertEquals("mail@test.com", datos.getEmail());
  }

  @Test
  void deberiaSetearPasswordCorrectamente() {
    DatosLogin datos = new DatosLogin();

    datos.setPassword("123456");
    assertEquals("123456", datos.getPassword());
  }

  @Test
  public void consultarUsuarioDebeRetornarNullSiUsuarioNoExiste() {

    RepositoryUsuario repo = Mockito.mock(RepositoryUsuario.class);
    ServiceLoginImpl service = new ServiceLoginImpl(repo);

    when(repo.buscar("test@mail.com")).thenReturn(null);
    Usuario resultado = service.consultarUsuario("test@mail.com","Pass123!");

    assertNull(resultado);
  }

  @Test
  public void consultarUsuarioDebeRetornarNullSiUsuarioEstaInactivo() {

    RepositoryUsuario repo = Mockito.mock(RepositoryUsuario.class);
    ServiceLoginImpl service = new ServiceLoginImpl(repo);

    Usuario usuario = new Usuario();
    usuario.setActivo(false);

    when(repo.buscar("test@mail.com")).thenReturn(usuario);
    Usuario resultado = service.consultarUsuario("test@mail.com","Pass123!");

    assertNull(resultado);
  }

  @Test
  public void consultarUsuarioDebeRetornarNullSiPasswordEsIncorrecta() {

    RepositoryUsuario repo = Mockito.mock(RepositoryUsuario.class);
    ServiceLoginImpl service = new ServiceLoginImpl(repo);

    Usuario usuario = new Usuario();
    usuario.setActivo(true);
    usuario.setPassword("Correcta123!");

    when(repo.buscar("test@mail.com")).thenReturn(usuario);
    Usuario resultado = service.consultarUsuario("test@mail.com","Incorrecta123!");

    assertNull(resultado);
  }

  @Test
  public void consultarUsuarioDebeRetornarUsuarioSiDatosSonCorrectos() {

    RepositoryUsuario repo = Mockito.mock(RepositoryUsuario.class);
    ServiceLoginImpl service = new ServiceLoginImpl(repo);

    Usuario usuario = new Usuario();
    usuario.setActivo(true);
    usuario.setPassword("Pass123!");

    when(repo.buscar("test@mail.com")).thenReturn(usuario);
    Usuario resultado = service.consultarUsuario("test@mail.com", "Pass123!");

    assertEquals(usuario, resultado);
  } 

  @Test
  public void registrarDebeLanzarExcepcionSiUsuarioYaExiste() {

    RepositoryUsuario repo = Mockito.mock(RepositoryUsuario.class);
    ServiceLoginImpl service = new ServiceLoginImpl(repo);

    Usuario usuario = new Usuario();
    usuario.setEmail("test@mail.com");

    when(repo.buscar("test@mail.com")).thenReturn(usuario);

    assertThrows(
      UsuarioExistente.class,
      () -> service.registrar(usuario));
  }

  @Test
  public void registrarDebeGuardarUsuarioSiNoExiste() throws UsuarioExistente {

    RepositoryUsuario repo = Mockito.mock(RepositoryUsuario.class);
    ServiceLoginImpl service = new ServiceLoginImpl(repo);

    Usuario usuario = new Usuario();
    usuario.setEmail("test@mail.com");

    when(repo.buscar("test@mail.com")).thenReturn(null);
    service.registrar(usuario);

    verify(repo).guardar(usuario);
  }
}
