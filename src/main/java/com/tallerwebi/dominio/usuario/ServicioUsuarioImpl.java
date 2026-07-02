package com.tallerwebi.dominio.usuario;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    private RepositoryUsuario repositoryUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositoryUsuario repositoryUsuario) {
        this.repositoryUsuario = repositoryUsuario;
    }

    @Override
    public List<Usuario> listarTodos() {
        return repositoryUsuario.listarTodos();
    }

    @Override
    public List<Usuario> listarJugadores() {
        return repositoryUsuario.listarJugadores();
    }

    @Override
    public Usuario buscar(Long id) {
        return repositoryUsuario.buscar(id);
    }

    @Override
    public void modificar(Usuario usuario) {
        repositoryUsuario.modificar(usuario);
    }

    @Override
    public void banear(Long id, Integer dias, String motivo) {

        Usuario usuario = repositoryUsuario.buscar(id);
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, dias);
        usuario.banear(calendar.getTime(), motivo);

        repositoryUsuario.modificar(usuario);
    }

    @Override
    public void banPermanente(Long id, String motivo) {

        Usuario usuario = repositoryUsuario.buscar(id);
        usuario.banPermanente(motivo);

        repositoryUsuario.modificar(usuario);
    }

    @Override
    public void desbanear(Long id) {

        Usuario usuario = repositoryUsuario.buscar(id);
        usuario.desbanear();

        repositoryUsuario.modificar(usuario);
    }

    @Override
    public void advertir(Long id) {

        Usuario usuario = repositoryUsuario.buscar(id);
        usuario.setAdvertencias(usuario.getAdvertencias()+1);

        // A las 3 advertencias se banea automáticamente por 7 días
        if(usuario.getAdvertencias()>=3){

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,7);

            usuario.banear(calendar.getTime(),"3 advertencias");
            usuario.setAdvertencias(0);
        }
        repositoryUsuario.modificar(usuario);
    }

}