package com.tallerwebi.presentacion.categoriaController;

import com.tallerwebi.dominio.Categoria.Categoria;
import com.tallerwebi.dominio.apiPregunta.ApiPregunta;
import com.tallerwebi.dominio.servicioCategoria.ServicioCategoria;
import com.tallerwebi.dominio.servicioPregunta.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/categoria")
public class ControllerCategoria {

    @Autowired
    private PreguntaService preguntaService;

    @Autowired
    private ServicioCategoria categoriaService;

    private List<Integer> categoriasUsadas = new ArrayList<>();
    private int puntaje = 0;
    private ApiPregunta preguntaActual = null;
    private int categoriaActualId = 0;
    private String nombreUsuario = "tmansilla7";

    private int cantidadPreguntasTotal = 0;
    private int preguntasRespondidas = 0;

    @GetMapping
    public String inicio(Model model) {
        int total = categoriaService.obtenerTotal();
        int restantes = total - categoriasUsadas.size();

        model.addAttribute("puntaje", puntaje);
        model.addAttribute("categoriasUsadas", categoriasUsadas.size());
        model.addAttribute("totalCategorias", total);
        model.addAttribute("categoriasRestantes", restantes);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "categoria-inicio";
    }

    @PostMapping("/obtener")
    public String obtenerCategoria(
            @RequestParam int cantidad,
            Model model) {

        if (cantidad < 1 || cantidad > 50) {
            cantidad = 10;
        }

        cantidadPreguntasTotal = cantidad;
        preguntasRespondidas = 0;

        Categoria categoria = categoriaService.obtenerCategoriaRandom(categoriasUsadas);

        if (categoria == null) {
            model.addAttribute("puntajeFinal", puntaje);
            model.addAttribute("totalCategorias", categoriaService.obtenerTotal());
            model.addAttribute("nombreUsuario", nombreUsuario);
            return "categoria-final";
        }

        List<ApiPregunta> preguntas = preguntaService.obtenerPreguntasPorCategoria(1, categoria.getId());

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoria.getId());
            return obtenerCategoria(cantidad, model);
        }

        ApiPregunta pregunta = preguntas.get(0);
        preguntaActual = pregunta;
        categoriaActualId = categoria.getId();

        model.addAttribute("pregunta", pregunta);
        model.addAttribute("categoria", categoria);
        model.addAttribute("categoriaId", categoria.getId());
        model.addAttribute("puntaje", puntaje);
        model.addAttribute("preguntasRespondidas", preguntasRespondidas + 1);
        model.addAttribute("cantidadPreguntasTotal", cantidadPreguntasTotal);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "categoria-pregunta";
    }

    @PostMapping("/responder")
    public String responder(
            @RequestParam String respuesta,
            @RequestParam int categoriaId,
            Model model) {

        if (preguntaActual == null || categoriaActualId != categoriaId) {
            return "redirect:/categoria";
        }

        Categoria categoria = categoriaService.obtenerPorId(categoriaId);

        boolean acierto = respuesta.equals(preguntaActual.getRespuestaCorrectaDecodificada());

        if (acierto) {
            puntaje++;
        }

        String respuestaCorrecta = preguntaActual.getRespuestaCorrectaDecodificada();

        preguntasRespondidas++;

        if (preguntasRespondidas >= cantidadPreguntasTotal) {
            categoriasUsadas.add(categoriaId);
            preguntaActual = null;

            model.addAttribute("acierto", acierto);
            model.addAttribute("categoria", categoria);
            model.addAttribute("puntaje", puntaje);
            model.addAttribute("esUltimaPregunta", true);
            model.addAttribute("nombreUsuario", nombreUsuario);

            if (!acierto) {
                model.addAttribute("respuestaCorrecta", respuestaCorrecta);
            }

            return "categoria-resultado";
        }

        List<ApiPregunta> siguientes = preguntaService.obtenerPreguntasPorCategoria(1, categoriaId);

        if (siguientes.isEmpty()) {
            categoriasUsadas.add(categoriaId);

            model.addAttribute("acierto", acierto);
            model.addAttribute("categoria", categoria);
            model.addAttribute("puntaje", puntaje);
            model.addAttribute("esUltimaPregunta", true);
            model.addAttribute("nombreUsuario", nombreUsuario);

            if (!acierto) {
                model.addAttribute("respuestaCorrecta", respuestaCorrecta);
            }

            return "categoria-resultado";
        }

        ApiPregunta siguiente = siguientes.get(0);
        preguntaActual = siguiente;

        model.addAttribute("acierto", acierto);
        model.addAttribute("categoria", categoria);
        model.addAttribute("puntaje", puntaje);
        model.addAttribute("esUltimaPregunta", false);
        model.addAttribute("preguntasRespondidas", preguntasRespondidas);
        model.addAttribute("cantidadPreguntasTotal", cantidadPreguntasTotal);
        model.addAttribute("nombreUsuario", nombreUsuario);

        if (!acierto) {
            model.addAttribute("respuestaCorrecta", respuestaCorrecta);
        }

        return "categoria-resultado";
    }

    @PostMapping("/reiniciar")
    public String reiniciar() {
        categoriasUsadas.clear();
        puntaje = 0;
        preguntaActual = null;
        categoriaActualId = 0;
        cantidadPreguntasTotal = 0;
        preguntasRespondidas = 0;
        return "redirect:/categoria";
    }
}