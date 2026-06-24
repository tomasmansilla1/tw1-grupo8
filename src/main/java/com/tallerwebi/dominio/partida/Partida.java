package com.tallerwebi.dominio.partida;

import com.tallerwebi.dominio.juego.Respuesta;
import com.tallerwebi.dominio.usuario.Usuario;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.*;

@Entity
public class Partida {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String categoria;
  @Column(nullable = false)
  private LocalDateTime fecha;
  @Column(nullable = false)
  private Integer puntajeObtenido;
  @Column(name = "inicio_partida", columnDefinition = "TIME")
  private LocalTime inicioPartida;
  @Column(name = "final_partida", columnDefinition = "TIME")
  private LocalTime finalPartida;

  @Column(nullable = false)
  private Boolean esVictoria;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "respuesta_id")
  private Respuesta respuesta;


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

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalTime getInicioPartida() {
        return inicioPartida;
    }

    public void setInicioPartida(LocalTime inicioPartida) {
        this.inicioPartida = inicioPartida;
    }

    public LocalTime getFinalPartida() {
        return finalPartida;
    }

    public void setFinalPartida(LocalTime finalPartida) {
        this.finalPartida = finalPartida;
    }
}
