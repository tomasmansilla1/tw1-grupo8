package com.tallerwebi.dominio.usuario;

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
}