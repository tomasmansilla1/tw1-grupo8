package com.tallerwebi.repository;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.model.Pregunta;

@Repository
@Transactional
public class PreguntasRepositoryImpl implements PreguntasRepository {

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