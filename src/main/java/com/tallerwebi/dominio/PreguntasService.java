package com.tallerwebi.dominio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreguntasService {

    @Autowired
    private RepositorioPreguntas repository;

    public PreguntasService(RepositorioPreguntas repositorioPreguntas) {
        this.repository = repositorioPreguntas;
    }

    // Guardar una pregunta
    public void guardar(Pregunta pregunta) {
        repository.save(pregunta);
    }

    // Listar todas las preguntas
    public List<Pregunta> listar() {
        return repository.findAll();
    }

    // Obtener pregunta por ID
    public Pregunta obtenerPorId(Long id) {
        return repository.findById(id);
    }

    // eliminar pregunta
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}