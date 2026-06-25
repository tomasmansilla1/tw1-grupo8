package com.tallerwebi.dominio.pregunta;

import java.util.List;

public interface RepositoryPreguntas {
    void save(Pregunta pregunta);
    List<Pregunta> findAll();
    Pregunta findById(Long id);
    void deleteById(Long id);
    List<Pregunta> buscarPorCategoria(String categoria);
}
