package com.tallerwebi.dominio.registro;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.usuario.Usuario;

@Service
public class ServicioRegistroImpl implements ServicioRegistro {

  private List<Usuario> jugadores = new ArrayList<>();
  private static final int LONGITUD_MINIMA_PASSWORD = 8;
  private static final String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

  public ServicioRegistroImpl() {}

  @Override
  public Usuario registrar(String email, String username, String password) {
    validarEmail(email);
    validarUsername(username);
    validarPassword(password);
    validarQueNoExistaElEmail(email);
    validarQueNoExistaElUsername(username);

    Usuario jugador = new Usuario(email, username, password);
    jugadores.add(jugador);

    return jugador;
  }

  public void validarEmail(String email) {
    if (email == null) {
      throw new IllegalArgumentException("El email no puede estar vacío");
    }
    if (!Pattern.matches(regex, email)) {
      throw new IllegalArgumentException("El email no tiene un formato valido");
    }
  }

  public void validarUsername(String username) {
    if (username == null) {
      throw new IllegalArgumentException("El username no puede estar vacío");
    }
  }

  public void validarPassword(String password) {
    if (password == null) {
      throw new IllegalArgumentException("El password no puede estar vacío");
    }
    if (!elPasswordTieneFormatoValido(password)) {
      throw new IllegalArgumentException("El password no cumple formato");
    }
  }

  private boolean elPasswordTieneFormatoValido(String password) {
    return (
      elPasswordTieneLongitudValida(password) &&
      elPasswordContieneMayuscula(password) &&
      elPasswordContieneCaracterEspecial(password) &&
      elPasswordContieneNumero(password)
    );
  }

  private boolean elPasswordTieneLongitudValida(String password) {
    return password.length() >= LONGITUD_MINIMA_PASSWORD;
  }

  private boolean elPasswordContieneMayuscula(String password) {
    for (int i = 0; i < password.length(); i++) {
      if (Character.isUpperCase(password.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  private boolean elPasswordContieneCaracterEspecial(String password) {
    for (int i = 0; i < password.length(); i++) {
      if (!Character.isLetterOrDigit(password.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  private boolean elPasswordContieneNumero(String password) {
    for (int i = 0; i < password.length(); i++) {
      if (Character.isDigit(password.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  private void validarQueNoExistaElEmail(String email) {
    for (Usuario jugador : jugadores) {
        if (jugador.getEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("El email ya existe");
        }
    }
  } 

  //  private void validarQueExistaElEmail(String email) {
  //    for (Jugador jugador : jugadores) {
  //      if (jugador.getEmail().equalsIgnoreCase(email)) {
  //        return;
  //      }
  //    }
  //    throw new IllegalArgumentException("El email no existe");
  //  }

  private void validarQueNoExistaElUsername(String username) {
    for (Usuario jugador : jugadores) {
      if (jugador.getUsername().equalsIgnoreCase(username)) {
        throw new IllegalArgumentException("El username ya existe");
      }
    }
  }

  //  private void validarQueElIngresoDelPasswordSeaCorrecto(String password) {
  //    for (Jugador jugador : jugadores) {
  //      if (jugador.getPassword().equalsIgnoreCase(password)) {
  //        return;
  //      }
  //    }
  //    throw new IllegalArgumentException("El password no coincide con el email registrado");
  //  }

  @Override
  public void iniciarSesion(String email, String password) {
    for (Usuario jugador : jugadores) {
      if (
        jugador.getEmail().equalsIgnoreCase(email) &&
        jugador.getPassword().equalsIgnoreCase(password)
      ) {
        return;
      }
    }
    throw new IllegalArgumentException("Email o password incorrectos.");
  }

}