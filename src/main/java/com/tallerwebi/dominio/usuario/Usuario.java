package com.tallerwebi.dominio.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String rol;
  
  private Boolean activo = false;

  private Integer puntaje;

  private Integer partidasGanadasSeguidas;

  // constructor vacio
  public Usuario() {
  }

  public Usuario(String email, String username, String password) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.activo = true;
    this.puntaje = 0;
    this.partidasGanadasSeguidas = 0;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  public void activar() {
    activo = true;
  }

  public Integer getPuntaje() {
    return puntaje;
  }

  public void setPuntaje(Integer puntaje) {
    this.puntaje = puntaje;
  }

  public Integer getPartidasGanadasSeguidas() {
    return partidasGanadasSeguidas;
  }
  public void setPartidasGanadasSeguidas(Integer partidasGanadasSeguidas) {
    this.partidasGanadasSeguidas = partidasGanadasSeguidas;
  }

  private Integer puntajeTotal = 0;

  public Integer getPuntajeTotal() {
    return puntajeTotal;
  }

  public void setPuntajeTotal(Integer puntajeTotal) {
    this.puntajeTotal = puntajeTotal;
  }

  public void sumarPuntos(Integer puntos) {
    if (this.puntajeTotal == null) {
      this.puntajeTotal = 0;
    }
    this.puntajeTotal += puntos;
  }
}
