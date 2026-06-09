package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Pregunta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String categoria;
  private String enunciado;

  private String opcionA;
  private String opcionB;
  private String opcionC;
  private String opcionD;

  private String opcionCorrecta;

  // --- GETTERS Y SETTERS ---

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getEnunciado() {
    return enunciado;
  }

  public void setEnunciado(String enunciado) {
    this.enunciado = enunciado;
  }

  public String getOpcionA() {
    return opcionA;
  }

  public void setOpcionA(String opcionA) {
    this.opcionA = opcionA;
  }

  public String getOpcionB() {
    return opcionB;
  }

  public void setOpcionB(String opcionB) {
    this.opcionB = opcionB;
  }

  public String getOpcionC() {
    return opcionC;
  }

  public void setOpcionC(String opcionC) {
    this.opcionC = opcionC;
  }

  public String getOpcionD() {
    return opcionD;
  }

  public void setOpcionD(String opcionD) {
    this.opcionD = opcionD;
  }

  public String getOpcionCorrecta() {
    return opcionCorrecta;
  }

  public void setOpcionCorrecta(String opcionCorrecta) {
    this.opcionCorrecta = opcionCorrecta;
  }
}
