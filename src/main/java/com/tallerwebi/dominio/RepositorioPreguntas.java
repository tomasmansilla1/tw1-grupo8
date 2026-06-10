package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPreguntas {
    void save(Pregunta pregunta);
    List<Pregunta> findAll();
    Pregunta findById(Long id);
    void deleteById(Long id);
}