package com.tallerwebi.infraestructura.categoriaDia;

import java.util.List;

import com.tallerwebi.dominio.categoriaDia.CategoriaHistorial;

public interface CategoriaRepository {
    CategoriaHistorial findUltima();
    List<CategoriaHistorial> findAll();
    void save(CategoriaHistorial categoria);
}