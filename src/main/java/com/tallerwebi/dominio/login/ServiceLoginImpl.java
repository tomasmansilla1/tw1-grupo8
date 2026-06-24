package com.tallerwebi.dominio.login;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioLogin")
@Transactional
public class ServiceLoginImpl implements ServiceLogin {

  private RepositoryUsuario repositorioUsuario;

  @Autowired
  public ServiceLoginImpl(RepositoryUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public Usuario consultarUsuario(String email, String password) {
    Usuario usuario = repositorioUsuario.buscar(email);

    if (usuario == null) {
      return null;
    }
    if(!usuario.getActivo()){
      return null;
    }
    if (!usuario.getPassword().equals(password)) {
      return null;
    }
    return usuario;
  }

  @Override
  public void registrar(Usuario usuario) throws UsuarioExistente {
    if (repositorioUsuario.buscar(usuario.getEmail()) != null) {
      throw new UsuarioExistente();
    }
    repositorioUsuario.guardar(usuario);
  }
}
