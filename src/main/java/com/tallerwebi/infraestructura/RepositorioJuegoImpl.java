package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioJuego;
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

    @SuppressWarnings({ "deprecation", "unchecked" })
    @Override
    public List<Pregunta> buscarPreguntaPorCategoria(String categoria) {
        return sessionFactory.getCurrentSession().createCriteria(Pregunta.class)
            .add(Restrictions.eq("categoria", categoria)).list();
    }

    @Override
    public void guardarPartida(Partida partida) {
        sessionFactory.getCurrentSession().save(partida);
    }

    @Override
    public List<Partida> buscarPartidasPorUsuario(Long usuarioId) {

        return sessionFactory.getCurrentSession().createQuery(
                "FROM Partida p WHERE p.usuario.id = :id ORDER BY p.fecha DESC",
                Partida.class
            ).setParameter("id", usuarioId).list();
    }
}
