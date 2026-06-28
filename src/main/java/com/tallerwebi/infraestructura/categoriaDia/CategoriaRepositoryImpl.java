package com.tallerwebi.infraestructura.categoriaDia;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.categoriaDia.CategoriaHistorial;

@Repository
@Transactional
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private SessionFactory sessionFactory;

    // constructor
    @Autowired
    public CategoriaRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CategoriaHistorial findUltima() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
            "FROM CategoriaHistorial c ORDER BY c.fecha DESC, c.id DESC",
            CategoriaHistorial.class
        ).setMaxResults(1).uniqueResult();
    }

    @Override
    public List<CategoriaHistorial> findAll() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
            "FROM CategoriaHistorial c ORDER BY c.fecha DESC, c.id DESC",
            CategoriaHistorial.class
        ).list();
    }

    @Override
    public void save(CategoriaHistorial categoria) {
        Session session = sessionFactory.getCurrentSession();
        session.save(categoria);
    }
}