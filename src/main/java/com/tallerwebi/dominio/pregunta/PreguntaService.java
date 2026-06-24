package com.tallerwebi.dominio.pregunta;

import java.util.List;

public interface PreguntaService {
    void guardar(Pregunta pregunta);
    List<Pregunta> listar();
    Pregunta obtenerPorId(Long id);
    void eliminar(Long id);
    List<Pregunta> obtenerPorCategoria(String categoria);
}