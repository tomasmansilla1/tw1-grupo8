package com.tallerwebi.dominio.registro;

import com.tallerwebi.dominio.usuario.Usuario;

public interface ServicioRegistro {
  Usuario registrar(String email, String username, String password);
 
  void validarEmail(String email);
  void validarUsername(String username);
  void validarPassword(String password);
}
