package com.tallerwebi.dominio.partida;

import com.tallerwebi.dominio.usuario.Usuario;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "partidas")
public class Partida {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime fecha;

  @Column(nullable = false)
  private Integer puntajeObtenido;

  @Column(nullable = false)
  private Boolean esVictoria;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  // --- GETTERS Y SETTERS ---
  public Partida() {

  }
  public Partida(LocalDateTime fecha, Integer puntajeObtenido, Boolean esVictoria, Usuario usuario) {
    this.fecha = fecha;
    this.puntajeObtenido = puntajeObtenido;
    this.esVictoria = esVictoria;
    this.usuario = usuario;
  }

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
