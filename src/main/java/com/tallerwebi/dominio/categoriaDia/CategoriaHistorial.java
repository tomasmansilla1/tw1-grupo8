package com.tallerwebi.dominio.categoriaDia;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categoria_historial")
public class CategoriaHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "idapi_nombre")
    private Integer apiIdNombre;

    @Column(nullable = false)
    private LocalDate fecha;

    // constructor vacío
    public CategoriaHistorial() {
    }

    public CategoriaHistorial(String nombre, Integer apiIdNombre ,LocalDate fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.apiIdNombre = apiIdNombre;
    }

    // getters y setters
    public Integer getApiIdNombre() {
        return apiIdNombre;
    }

    public void setApiIdNombre(Integer apiIdNombre) {
        this.apiIdNombre = apiIdNombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}