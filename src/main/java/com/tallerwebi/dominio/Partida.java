package com.tallerwebi.dominio;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
public class Partida {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime fecha;
  private Integer puntajeObtenido;
  private Boolean esVictoria;

  // conecta partida con usuario (muchas partidas pueden pertenecer a un mismo
  // usuario)
  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  // --- GETTERS Y SETTERS ---

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public void setFecha(LocalDateTime fecha) {
    this.fecha = fecha;
  }

  public Integer getPuntajeObtenido() {
    return puntajeObtenido;
  }

  public void setPuntajeObtenido(Integer puntajeObtenido) {
    this.puntajeObtenido = puntajeObtenido;
  }

  public Boolean getEsVictoria() {
    return esVictoria;
  }

  public void setEsVictoria(Boolean esVictoria) {
    this.esVictoria = esVictoria;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
}
