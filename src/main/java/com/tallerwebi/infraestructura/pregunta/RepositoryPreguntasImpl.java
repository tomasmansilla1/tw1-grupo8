package com.tallerwebi.infraestructura.pregunta;

import com.tallerwebi.dominio.pregunta.Pregunta;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.pregunta.RepositoryPreguntas;

@Repository
@Transactional
public class RepositoryPreguntasImpl implements RepositoryPreguntas {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositoryPreguntasImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

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

  @Override
  public List<Pregunta> buscarPorCategoria(String categoria) {

    return sessionFactory.getCurrentSession().createQuery(
      "FROM Pregunta WHERE categoria = :categoria",
      Pregunta.class
    )
    .setParameter("categoria", categoria).list();
  }
}
