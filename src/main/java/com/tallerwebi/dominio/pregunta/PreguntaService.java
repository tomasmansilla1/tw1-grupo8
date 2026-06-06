package com.tallerwebi.dominio.pregunta;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface PreguntaService {

    void guardar(Pregunta pregunta);

    List<Pregunta> listar();

    Pregunta obtenerPorId(Long id);

    void eliminar(Long id);
}