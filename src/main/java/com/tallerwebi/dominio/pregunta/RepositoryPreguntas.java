package com.tallerwebi.dominio.pregunta;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryPreguntas {
    void save(Pregunta pregunta);
    List<Pregunta> findAll();
    Pregunta findById(Long id);
    void deleteById(Long id);
}
