package com.tallerwebi.service;

import com.tallerwebi.exception.UsuarioExistente;
import com.tallerwebi.model.Usuario;

public interface ServicioLogin {
  Usuario consultarUsuario(String email, String password);
  void registrar(Usuario usuario) throws UsuarioExistente;
}
