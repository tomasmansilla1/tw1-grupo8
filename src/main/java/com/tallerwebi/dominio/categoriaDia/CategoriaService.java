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

    public CategoriaHistorial obtenerIdApiPregunta() {
        CategoriaHistorial idApi = repositorio.buscarIdApiPreguntaCategoriaDia();

        if (idApi == null) {
            return null;
        }

        return idApi;
    }

    public List<CategoriaHistorial> obtenerHistorial() {
        return repositorio.findAll();
    }

    public void guardarNuevaCategoria(String nombre) {
        CategoriaHistorial ultima = repositorio.findUltima();

        if (ultima != null && ultima.getNombre().equalsIgnoreCase(nombre)) {
            return;
        }

        Integer idApi;

        switch (nombre) {
            case "Deportes":
                idApi = 21;
                break;
            case "Ciencia":
                idApi = 17;
                break;
            case "Historia":
                idApi = 23;
                break;
            case "Entretenimiento":
                idApi = 9;
                break;
            default:
                throw new RuntimeException("Categoría inválida");
        }

        CategoriaHistorial nueva = new CategoriaHistorial(nombre, idApi ,LocalDate.now());
        
        repositorio.save(nueva);
    }   
}