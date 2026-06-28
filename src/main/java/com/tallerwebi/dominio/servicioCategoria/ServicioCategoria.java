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
        todas.add(new Categoria(10, "Entertainment: Books"));
        todas.add(new Categoria(11, "Entertainment: Film"));
        todas.add(new Categoria(12, "Entertainment: Music"));
        todas.add(new Categoria(13, "Musicals & Theatres"));
        todas.add(new Categoria(14, "Television"));
        todas.add(new Categoria(15, "Video Games"));
        todas.add(new Categoria(16, "Board Games"));
        todas.add(new Categoria(17, "Nature & Science"));
        todas.add(new Categoria(18, "Computers"));
        todas.add(new Categoria(19, "Mathematics"));
        todas.add(new Categoria(20, "Mythology"));
        todas.add(new Categoria(21, "Sports"));
        todas.add(new Categoria(22, "Geography"));
        todas.add(new Categoria(23, "History"));
        todas.add(new Categoria(24, "Politics"));
        todas.add(new Categoria(25, "Art"));
        todas.add(new Categoria(26, "Celebrities"));
        todas.add(new Categoria(27, "Animals"));
        todas.add(new Categoria(28, "Vehicles"));
        todas.add(new Categoria(29, "Entertainment: Comics"));
        todas.add(new Categoria(30, "Science & Nature"));
        todas.add(new Categoria(31, "Science: Gadgets"));
        todas.add(new Categoria(32, "Anime & Manga"));
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
