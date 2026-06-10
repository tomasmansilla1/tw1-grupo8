package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.RepositorioRanking;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioRankingImpl implements RepositorioRanking {

    SessionFactory sessionFactory;

    @Autowired
    public RepositorioRankingImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Partida> buscarUsuariosPorRanking(String categoria) {
        return sessionFactory.getCurrentSession().createCriteria(Partida.class)
                .add(Restrictions.eq("categoria", categoria))
                .list();
    }
}
