package com.tallerwebi.model;

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
    private String pregunta;
    // opciones: no puede estar vacio
    @Column(nullable = false)
    private String a;
    @Column(nullable = false)
    private String b;
    @Column(nullable = false)
    private String c;
    @Column(nullable = false)
    private String d;

    // pregunta correcta: no puede estar vacio
    @Column(nullable = false)
    private String correcta;

    // Constructor vacío
    public Pregunta() {
    }

    // GETTERS Y SETTERS
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    // pregunta
    public String getPregunta() {
        return pregunta;
    }
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
    // pregunta correcta
    public String getCorrecta() {
        return correcta;
    }
    public void setCorrecta(String correcta) {
        this.correcta = correcta;
    }
    // opciones
    public String getA() {
        return a;
    }
    public void setA(String a) {
        this.a = a;
    }
    public String getB() {
        return b;
    }
    public void setB(String b) {
        this.b = b;
    }
    public String getC() {
        return c;
    }
    public void setC(String c) {
        this.c = c;
    }
    public String getD() {
        return d;
    }
    public void setD(String d) {
        this.d = d;
    }
    // CATEGORIA
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}