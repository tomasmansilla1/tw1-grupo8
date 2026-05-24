package com.tallerwebi.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tallerwebi.model.Pregunta;

@Repository
public interface PreguntasRepository {
    void save(Pregunta pregunta);
    List<Pregunta> findAll();
    Pregunta findById(Long id);
    void deleteById(Long id);
}