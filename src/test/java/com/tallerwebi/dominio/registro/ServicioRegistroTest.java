package com.tallerwebi.dominio.registro;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.usuario.Usuario;


public class ServicioRegistroTest {

  ServicioRegistro servicioRegistro;
  String email = "tomas@gmail.com";
  String username = "tmansilla7";
  String password = "123456Ddw.";

  @BeforeEach
  void setUp() {
    servicioRegistro = new ServicioRegistroImpl(null);
  }

  @Test
  public void siIngresoMailYPasswordValidosElRegistroEsExitoso() {
    Usuario jugador = whenRegistroUsuario(email, username, password);

    thenElRegistroEsExitoso(jugador);
  }

  private Usuario whenRegistroUsuario(String email, String username, String password) {
    return servicioRegistro.registrar(email, username, password);
  }

  private void thenElRegistroEsExitoso(Usuario jugador) {
    assertThat(jugador, is(notNullValue()));
  }

  @Test
  public void siIngresoPasswordInvalidoElRegistroFalla() {
    assertThrows(
      IllegalArgumentException.class,
      () -> whenRegistroUsuario(email, username, "1234")
    );
  }

  @Test
  public void deberiaSetearUsernameCorrectamente() {
    DatosRegistroDTO datos = new DatosRegistroDTO();

    datos.setUsername("usuario123");
    assertEquals("usuario123",datos.getUsername());
  }

  @Test
  public void deberiaSetearPasswordCorrectamente() {
    DatosRegistroDTO datos = new DatosRegistroDTO();

    datos.setPassword("Password123.");
    assertEquals("Password123.",datos.getPassword());
  }

  //EXCEPCIONES
  @Test
  void validarEmailNullLanzaExcepcion() {
    IllegalArgumentException ex = assertThrows(
      IllegalArgumentException.class,
      () -> servicioRegistro.validarEmail(null)
    );
    assertEquals("El email no puede estar vacío", ex.getMessage());
  }

  @Test
  void validarEmailConFormatoInvalidoLanzaExcepcion() {
    IllegalArgumentException ex = assertThrows(
      IllegalArgumentException.class,
      () -> servicioRegistro.validarEmail("mail-invalido")
    );

    assertEquals("El email no tiene un formato valido", ex.getMessage());
  }

  @Test
  void registrar_usernameNuevo_noLanzaExcepcion() {
    ServicioRegistro servicioRegistro = new ServicioRegistroImpl(null);

    assertDoesNotThrow(()->servicioRegistro.registrar(
      "mail@mail.com", 
      "facu", 
      "Pass123!")
    );
  }
  @Test
  void validarUsernameNullLanzaExcepcion() {
    IllegalArgumentException ex = assertThrows(
      IllegalArgumentException.class,
      () -> servicioRegistro.validarUsername(null)
    );

    assertEquals("El username no puede estar vacío", ex.getMessage());
  }
  @Test
  void registrarUsernameDuplicadoLanzaExcepcion() {
    ServicioRegistro servicioRegistro = new ServicioRegistroImpl(null);

    servicioRegistro.registrar("mail1@mail.com", "facu", "Pass123!");

    IllegalArgumentException ex = assertThrows(
      IllegalArgumentException.class,
      () -> servicioRegistro.registrar(
        "mail2@mail.com", 
        "facu", 
        "Pass123!"
      )
    );

    assertEquals("El username ya existe", ex.getMessage());
  }

  @Test
  void validarPasswordNullLanzaExcepcion() {
    IllegalArgumentException ex = assertThrows(
      IllegalArgumentException.class,
      () -> servicioRegistro.validarPassword(null)
    );

    assertEquals("El password no puede estar vacío", ex.getMessage());
  }
  @Test
  void validarPasswordSinMayusculaLanzaExcepcion() {
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioRegistro.registrar("test@mail.com", "user", "password1!")
    );
  }
  @Test
  void validarPasswordSinNumeroLanzaExcepcion() {
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioRegistro.registrar("test@mail.com", "user", "Password!")
    );
  }
  @Test
  void validarPasswordSinCaracterEspecialLanzaExcepcion() {
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioRegistro.registrar("test@mail.com", "user", "Password1")
    );
  }
  @Test
  void validarPasswordMenorA8CaracteresLanzaExcepcion() {
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioRegistro.registrar("test@mail.com", "user", "P1!")
    );
  } 

  
  //GETTERS Y SETTERS
  @Test
  public void deberiaCrearObjetoConConstructorVacio() {
    DatosRegistroDTO datos = new DatosRegistroDTO();

    assertNull(datos.getEmail());
    assertNull(datos.getUsername());
    assertNull(datos.getPassword());
  }

  @Test
  public void deberiaSetearEmailCorrectamente() {
    DatosRegistroDTO datos = new DatosRegistroDTO();

    datos.setEmail("test@gmail.com");
    assertEquals("test@gmail.com", datos.getEmail());
  }

  @Test
  public void queSePuedaCrearDatosRegistroDTO() {
    DatosRegistroDTO dto = new DatosRegistroDTO(
      "test@mail.com",
      "usuario",
      "Pass123!"
    );

    assertEquals("test@mail.com", dto.getEmail());
    assertEquals("usuario", dto.getUsername());
    assertEquals("Pass123!", dto.getPassword());
  }

  
}
