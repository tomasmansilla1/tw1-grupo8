package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class PartidaTest {

    @Test
    public void alCrearUnaPartidaDeberiaPoderSetearSusValores() {
        // Preparación (Dado)
        Partida partida = new Partida();
        LocalDateTime fecha = LocalDateTime.now();
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");

        // Ejecución (Cuando)
        partida.setPuntajeObtenido(100);
        partida.setEsVictoria(true);
        partida.setFecha(fecha);
        partida.setUsuario(usuario);

        // Validación (Entonces)
        assertThat(partida.getPuntajeObtenido(), is(equalTo(100)));
        assertThat(partida.getEsVictoria(), is(equalTo(true)));
        assertThat(partida.getFecha(), is(equalTo(fecha)));
        assertThat(partida.getUsuario().getEmail(), is(equalTo("test@test.com")));
    }
}