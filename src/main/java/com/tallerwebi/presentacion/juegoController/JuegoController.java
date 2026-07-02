package com.tallerwebi.presentacion.juegoController;

import com.tallerwebi.dominio.apiPregunta.ApiPregunta;
import com.tallerwebi.dominio.servicioPregunta.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.ArrayList;

@Controller
public class JuegoController {

    @Autowired
    private PreguntaService preguntaService;

    private List<ApiPregunta> preguntasActuales = new ArrayList<>();
    private int preguntaActual = 0;
    private int puntaje = 0;

    @GetMapping("/juego")
    public String inicio() {
        return "index";
    }

    @PostMapping("/iniciar-juego")
    public String iniciarJuego(
            @RequestParam(defaultValue = "10") int cantidad,
            @RequestParam(defaultValue = "") String dificultad,
            Model model) {

        try {
            if (dificultad.isEmpty()) {
                preguntasActuales = preguntaService.obtenerPreguntas(cantidad);
            } else {
                preguntasActuales = preguntaService.obtenerPreguntasPorDificultad(cantidad, dificultad);
            }

            preguntaActual = 0;
            puntaje = 0;

            model.addAttribute("totalPreguntas", preguntasActuales.size());
            model.addAttribute("preguntaNumero", preguntaActual + 1);
            model.addAttribute("pregunta", preguntasActuales.get(preguntaActual));

            return "juego";

        } catch (RuntimeException e) {
            model.addAttribute("error", "No se pudieron cargar las preguntas: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/responders")
    public String responder(
            @RequestParam String respuesta,
            Model model) {

        ApiPregunta pregunta = preguntasActuales.get(preguntaActual);

        if (respuesta.equals(pregunta.getRespuestaCorrectaDecodificada())) {
            puntaje++;
        }

        preguntaActual++;

        if (preguntaActual < preguntasActuales.size()) {
            model.addAttribute("totalPreguntas", preguntasActuales.size());
            model.addAttribute("preguntaNumero", preguntaActual + 1);
            model.addAttribute("pregunta", preguntasActuales.get(preguntaActual));
            model.addAttribute("puntajeActual", puntaje);
            return "juego";
        } else {
            // Fin del juego
            model.addAttribute("puntajeFinal", puntaje);
            model.addAttribute("totalPreguntas", preguntasActuales.size());
            model.addAttribute("porcentaje", (puntaje * 100) / preguntasActuales.size());
            return "resultado";
        }
    }
}
