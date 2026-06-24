package com.tallerwebi.infraestructura.juego;

import com.tallerwebi.dominio.juego.RepositorioJuego;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioJuegoImpl implements RepositorioJuego {

    SessionFactory sessionFactory;

    @Autowired
    public RepositorioJuegoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Pregunta> buscarPreguntaPorCategoria(String categoria) {
        return sessionFactory.getCurrentSession().createCriteria(Pregunta.class)
                .add(Restrictions.eq("categoria", categoria)).list();
    }

    @Override
    public void guardarPartida(Partida partida) {
        sessionFactory.getCurrentSession().save(partida);
    }
}
