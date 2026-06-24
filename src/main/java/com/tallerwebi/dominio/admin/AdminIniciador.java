package com.tallerwebi.dominio.admin;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Roles;
import com.tallerwebi.dominio.usuario.Usuario;

@Component
@Transactional
public class AdminIniciador {

    private RepositoryUsuario repositoryUsuario;

    @Autowired
    public AdminIniciador(RepositoryUsuario repositoryUsuario) {
        this.repositoryUsuario = repositoryUsuario;
    }

    public void crearAdmin() {

        Usuario admin = repositoryUsuario.buscar("admin@preguntados.com");

        if (admin == null) {
            Usuario nuevo = new Usuario();

            nuevo.setEmail("admin@preguntados.com");
            nuevo.setUsername("admin");
            nuevo.setPassword("Admin123!");
            nuevo.setRol(Roles.ADMIN);
            nuevo.setActivo(true);

            repositoryUsuario.guardar(nuevo);
        }
    }
}