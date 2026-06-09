package com.tallerwebi.dominio.pregunta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "preguntas")
public class Pregunta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // categoria: no puede estar vacio
  @Column(nullable = false)
  private String categoria;

  // columna de pregunta: no puede estar vacio
  @Column(nullable = false)
  private String textoPregunta;

  // opciones: no puede estar vacio
  @Column(nullable = false)
  private String opcionA;

  @Column(nullable = false)
  private String opcionB;

  @Column(nullable = false)
  private String opcionC;

  @Column(nullable = false)
  private String opcionD;

  // pregunta correcta: no puede estar vacio
  @Column(nullable = false)
  private String correcta;

  // Constructor vacío
  public Pregunta() {}

  // ID
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  // Pregunta
  public String getPregunta() {
    return textoPregunta;
  }

  public void setPregunta(String textoPregunta) {
    this.textoPregunta = textoPregunta;
  }

  // Respuesta correcta
  public String getCorrecta() {
    return correcta;
  }

  public void setCorrecta(String correcta) {
    this.correcta = correcta;
  }

  // Opción A
  public String getA() {
    return opcionA;
  }

  public void setA(String opcionA) {
    this.opcionA = opcionA;
  }

  // Opción B
  public String getB() {
    return opcionB;
  }

  public void setB(String opcionB) {
    this.opcionB = opcionB;
  }

  // Opción C
  public String getC() {
    return opcionC;
  }

  public void setC(String opcionC) {
    this.opcionC = opcionC;
  }

  // Opción D
  public String getD() {
    return opcionD;
  }

  public void setD(String opcionD) {
    this.opcionD = opcionD;
  }

  // Categoría
  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }
}
