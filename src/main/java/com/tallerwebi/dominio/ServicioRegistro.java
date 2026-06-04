package com.tallerwebi.dominio;

public interface ServicioRegistro {
  Jugador registrar(String email, String username, String password);
  void iniciarSesion(String email, String password);
}
