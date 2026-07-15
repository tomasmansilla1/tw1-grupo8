package com.tallerwebi.dominio.servicioCategoria;

import com.tallerwebi.dominio.Categoria.Categoria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ServicioCategoria {

    private List<Categoria> todas;

    public ServicioCategoria() {
        inicializarCategorias();
    }

    private void inicializarCategorias() {
        todas = new ArrayList<>();
        todas.add(new Categoria(9, "General Knowledge"));
        todas.add(new Categoria(17, "Nature & Science"));
        todas.add(new Categoria(21, "Sports"));
        todas.add(new Categoria(23, "History"));
    }


    public Categoria obtenerCategoriaRandom(List<Integer> categoriasUsadas) {
        List<Categoria> disponibles = new ArrayList<>();

        for (Categoria cat : todas) {
            if (!categoriasUsadas.contains(cat.getId())) {
                disponibles.add(cat);
            }
        }

        if (disponibles.isEmpty()) {
            return null;
        }

        int index = new Random().nextInt(disponibles.size());
        return disponibles.get(index);
    }

    public Categoria obtenerPorId(int id) {
        return todas.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public int obtenerTotal() {
        return todas.size();
    }
}
