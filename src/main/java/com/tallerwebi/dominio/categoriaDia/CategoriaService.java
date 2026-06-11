package com.tallerwebi.dominio.categoriaDia;

import java.util.List;

import com.tallerwebi.infraestructura.categoriaDia.CategoriaRepository;

public class CategoriaService {

    private CategoriaRepository repositorio;

    public CategoriaService(CategoriaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public String obtenerCategoriaActiva() {

        CategoriaHistorial ultima = repositorio.findUltima();

        if (ultima != null) {
            return ultima.getNombre();
        }

        return "Sin categoría";
    }

    public List<CategoriaHistorial> obtenerHistorial() {
        return repositorio.findAll();
    }

    public void guardarNuevaCategoria(String nombre) {

        CategoriaHistorial nueva = new CategoriaHistorial();
        nueva.setNombre(nombre);
        nueva.setFecha(java.time.LocalDate.now());

        repositorio.save(nueva);
    }
}