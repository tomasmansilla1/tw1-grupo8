package com.tallerwebi.config;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class SessionUtil {

    // verificar si hay sesión iniciada
    public boolean verificarSesion(HttpSession session) {

        Object usuario = session.getAttribute("usuario");
        return usuario != null;
    }

    // verificar si es admin
    public boolean verificarAdmin(HttpSession session) {

        // verificar sesión
        if (!verificarSesion (session) ) {
            return false;
        }

        // obtener rol
        Object rol = session.getAttribute("rol");

        // validar admin
        return rol != null && rol.equals("ADMIN");
    }
}