package com.tallerwebi.dominio.usuario;

public interface RepositoryUsuario {
  Usuario buscarUsuario(String email, String password);
  void guardar(Usuario usuario);
  Usuario buscar(String email);
  void modificar(Usuario usuario);
}
