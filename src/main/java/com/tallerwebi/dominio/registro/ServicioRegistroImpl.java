package com.tallerwebi.dominio.registro;

import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Roles;
import com.tallerwebi.dominio.usuario.Usuario;

@Service
@Transactional
public class ServicioRegistroImpl implements ServicioRegistro {
  private static final int LONGITUD_MINIMA_PASSWORD = 8;
  private static final String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

  private RepositoryUsuario repositorioUsuario;

  @Autowired
  public ServicioRegistroImpl(RepositoryUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  public ServicioRegistroImpl() {
   
  }

  @Override
  public Usuario registrar(String email, String username, String password) {

    validarEmail(email);
    validarUsername(username);
    validarPassword(password);

    if (repositorioUsuario.buscar(email) != null) {
      throw new IllegalArgumentException("El email ya existe");
    }
    if(repositorioUsuario.buscarPorUsername(username) != null){
      throw new IllegalArgumentException("El username ya existe");
    }

    Usuario usuario = new Usuario(email, username, password);

    usuario.setRol(Roles.JUGADOR);
    usuario.setActivo(true);
    usuario.setPuntaje(0);
    usuario.setPartidasGanadasSeguidas(0);

    repositorioUsuario.guardar(usuario);
    return usuario;
  }

  @Override
  public void validarEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("El email no puede estar vacío");
    }
    if (!Pattern.matches(regex, email)) {
      throw new IllegalArgumentException("El email no tiene un formato valido");
    }
  }

  @Override
  public void validarUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("El username no puede estar vacío");
    }
  }

  @Override
  public void validarPassword(String password) {
    if (password == null || password.trim().isEmpty()) {
      throw new IllegalArgumentException("El password no puede estar vacío");
    }
    if (!elPasswordTieneFormatoValido(password)) {
      throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial");
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
}