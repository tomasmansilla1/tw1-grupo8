package com.tallerwebi.dominio.categoriaDia;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.infraestructura.categoriaDia.CategoriaRepository;

@Service
@Transactional
public class CategoriaService {

    private CategoriaRepository repositorio;

    @Autowired
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
        CategoriaHistorial ultima = repositorio.findUltima();

        if (ultima != null && ultima.getNombre().equalsIgnoreCase(nombre)) {
            return;
        }
        CategoriaHistorial nueva = new CategoriaHistorial(nombre, LocalDate.now());
        
        repositorio.save(nueva);
    }   
}