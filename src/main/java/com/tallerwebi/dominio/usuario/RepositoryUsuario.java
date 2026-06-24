package com.tallerwebi.dominio.usuario;

import java.util.List;

public interface RepositoryUsuario {
  Usuario buscarUsuario(String email, String password);
  void guardar(Usuario usuario);
  Usuario buscar(String email);
  void modificar(Usuario usuario);
  Usuario buscarPorUsername(String username);
  List<Usuario> listarTodos();
}
