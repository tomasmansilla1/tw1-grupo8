package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego{

    @Override
    public Boolean opcionElegida(String opcionCorrecta, Pregunta pregunta) {
        Boolean validarOpcion = pregunta.getCorrecta().equalsIgnoreCase(opcionCorrecta);

        if (validarOpcion) {

        } else {

        }

        return validarOpcion;
    }


}
