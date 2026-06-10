package com.tallerwebi.dominio.registro;

public class DatosRegistroDTO {

  String email;
  String username;
  String password;

  public DatosRegistroDTO() {}

  public DatosRegistroDTO(String email, String username, String password) {
    this.email = email;
    this.username = username;
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
