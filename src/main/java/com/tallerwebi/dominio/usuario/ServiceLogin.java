package com.tallerwebi.dominio.usuario;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServiceLogin {
  Usuario consultarUsuario(String email, String password);
  void registrar(Usuario usuario) throws UsuarioExistente;
}
