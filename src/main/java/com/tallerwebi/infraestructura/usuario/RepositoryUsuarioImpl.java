package com.tallerwebi.infraestructura.usuario;

import com.tallerwebi.dominio.usuario.Usuario;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.usuario.RepositoryUsuario;

@Repository("repositorioUsuario")
public class RepositoryUsuarioImpl implements RepositoryUsuario {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositoryUsuarioImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @SuppressWarnings("deprecation")
  @Override
  public Usuario buscarUsuario(String email, String password) {
    /*
     * Se utiliza sessionFactory.getCurrentSession() directamente para que
     * el recurso sea gestionado por Spring y PMD no exija cerrarlo manualmente
     */
    return (Usuario) sessionFactory
        .getCurrentSession()
        .createCriteria(Usuario.class)
        .add(Restrictions.eq("email", email))
        .add(Restrictions.eq("password", password))
        .uniqueResult();
  }

  @Override
  public void guardar(Usuario usuario) {
    sessionFactory.getCurrentSession().save(usuario);
  }

  @SuppressWarnings("deprecation")
  @Override
  public Usuario buscar(String email) {
    return (Usuario) sessionFactory
        .getCurrentSession()
        .createCriteria(Usuario.class)
        .add(Restrictions.eq("email", email))
        .uniqueResult();
  }

  @Override
  public void modificar(Usuario usuario) {
    sessionFactory.getCurrentSession().update(usuario);
  }

  @SuppressWarnings("deprecation")
  @Override
  public Usuario buscarPorUsername(String username) {

    return (Usuario) sessionFactory.getCurrentSession()
      .createCriteria(Usuario.class)
      .add(Restrictions.eq("username",username)).uniqueResult();
  }

  @SuppressWarnings({ "unchecked", "deprecation" })
  @Override
  public List<Usuario> listarTodos() {
    return sessionFactory.getCurrentSession().createCriteria(Usuario.class).list();
  }

  @SuppressWarnings({ "deprecation", "unchecked" })
  public List<Usuario> obtenerTopUsuarios() {
    return sessionFactory.getCurrentSession()
        .createCriteria(Usuario.class)
        .addOrder(org.hibernate.criterion.Order.desc("puntajeTotal"))
        .setMaxResults(10)
        .list();
  }

  @Override
  @Transactional
  public Usuario buscar(Long id) {
    return (Usuario) sessionFactory.getCurrentSession().get(Usuario.class, id);
  }
}
