package com.tallerwebi.dominio.usuario;

import java.util.List;

public interface ServicioUsuario {

    List<Usuario> listarTodos();

    Usuario buscar(Long id);

    void banear(Long id, Integer dias, String motivo);
    void banPermanente(Long id, String motivo);
    void desbanear(Long id);
    void advertir(Long id);
    void modificar(Usuario usuario);

    List<Usuario> listarJugadores();
}
