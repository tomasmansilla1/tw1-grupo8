package com.tallerwebi.infraestructura.estadisticas;

import com.tallerwebi.dominio.estadisticas.RepositorioEstadisticas;
import com.tallerwebi.dominio.partida.Partida;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioEstadisticasImpl implements RepositorioEstadisticas {

    SessionFactory sessionFactory;

    @Autowired
    public RepositorioEstadisticasImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Partida> buscarPartidasFinalizadas() {
        return sessionFactory.getCurrentSession().createCriteria(Partida.class).list();
    }

    @Override
    public List<Partida> obtenerPartidasVictoriosas() {
        return sessionFactory.getCurrentSession().createCriteria(Partida.class)
                .add(Restrictions.eq("esVictoria", true)).list();
    }

}
