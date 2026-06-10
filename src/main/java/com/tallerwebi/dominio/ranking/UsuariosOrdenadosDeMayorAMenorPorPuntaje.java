package com.tallerwebi.dominio.ranking;

import com.tallerwebi.dominio.Usuario;

import java.util.Comparator;

public class UsuariosOrdenadosDeMayorAMenorPorPuntaje implements Comparator<Usuario> {

    //esto sirve para ordenar a los usuarios por su puntaje y mostrar en el ranking especifico
    // (de Progamacion basica II)
    @Override
    public int compare(Usuario o1, Usuario o2) {
        int resultado = o2.getPuntaje().compareTo(o1.getPuntaje());

        // Si tienen el mismo puntaje, desempatamos por ID para no borrarlos del TreeSet
        if (resultado == 0) {
            return o1.getId().compareTo(o2.getId());
        }
        return resultado;
    }
}
