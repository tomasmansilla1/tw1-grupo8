package com.tallerwebi.dominio.pregunta;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PreguntaServiceImpl implements PreguntaService {
    private RepositoryPreguntas repository;

    @Autowired
    public PreguntaServiceImpl(RepositoryPreguntas repository) {
        this.repository = repository;
    }

    // Guardar una pregunta
    @Override
    public void guardar(Pregunta pregunta) {
        repository.save(pregunta);
    }

    // Listar todas las preguntas
    @Override
    public List<Pregunta> listar() {
        return repository.findAll();
    }

    // Obtener pregunta por ID
    @Override
    public Pregunta obtenerPorId(Long id) {
        return repository.findById(id);
    }

    // Eliminar pregunta
    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Pregunta> obtenerPorCategoria(String categoria) {
        return repository.buscarPorCategoria(categoria);
    }


}
