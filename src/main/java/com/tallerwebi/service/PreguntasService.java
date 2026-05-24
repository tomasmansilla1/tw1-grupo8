package com.tallerwebi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.model.Pregunta;
import com.tallerwebi.repository.PreguntasRepository;

@Service
public class PreguntasService {
    @Autowired
    private PreguntasRepository repository;

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
    public void eliminar (Long id) {
        repository.deleteById(id);
    }
}