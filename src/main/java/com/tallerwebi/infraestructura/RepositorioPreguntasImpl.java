package com.tallerwebi.infraestructura;

import java.util.List;

import com.tallerwebi.dominio.RepositorioPreguntas;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.Pregunta;


@Repository
public class RepositorioPreguntasImpl implements RepositorioPreguntas {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Pregunta pregunta) {
        sessionFactory.getCurrentSession().saveOrUpdate(pregunta);
    }
    @Override
    public List<Pregunta> findAll() {
        return sessionFactory
            .getCurrentSession()
            .createQuery("FROM Pregunta", Pregunta.class)
            .getResultList();
    }
    @Override
    public Pregunta findById(Long id) {
        return sessionFactory.getCurrentSession().get(Pregunta.class, id);
    }
    @Override
    public void deleteById(Long id) {
        Pregunta pregunta = findById(id);

        if (pregunta != null) {
            sessionFactory.getCurrentSession().delete(pregunta);
        }
    }
}