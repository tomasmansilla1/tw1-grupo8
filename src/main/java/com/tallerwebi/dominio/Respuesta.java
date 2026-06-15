package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<String> respuestasUsuario;

    public Respuesta() {
        this.respuestasUsuario = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getRespuestasUsuario() {
        return respuestasUsuario;
    }

    public void setRespuestasUsuario(List<String> respuestasUsuario) {
        this.respuestasUsuario = respuestasUsuario;
    }
}
