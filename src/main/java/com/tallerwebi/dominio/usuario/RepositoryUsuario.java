package com.tallerwebi.dominio.usuario;

import java.util.List;

public interface RepositoryUsuario {
  Usuario buscarUsuario(String email, String password);

  void guardar(Usuario usuario);

  Usuario buscar(String email);

  // AGREGÁ ESTA LÍNEA
  Usuario buscar(Long id);

  void modificar(Usuario usuario);

  List<Usuario> obtenerTopUsuarios();
}