package com.tallerwebi.presentacion.categoriaController;

import com.tallerwebi.dominio.Categoria.Categoria;
import com.tallerwebi.dominio.apiPregunta.ApiPregunta;
import com.tallerwebi.dominio.servicioCategoria.ServicioCategoria;
import com.tallerwebi.dominio.servicioPregunta.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
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
            HttpSession session,
            Model model) {

        // Validar cantidad
        if (cantidad < 1 || cantidad > 50) {
            cantidad = 10;
        }

        // ← GUARDAR en sesión
        session.setAttribute("cantidadPreguntasTotal", cantidad);
        session.setAttribute("preguntasRespondidas", 0);

        // Obtener categoría random
        Categoria categoria = categoriaService.obtenerCategoriaRandom(categoriasUsadas);

        if (categoria == null) {
            model.addAttribute("puntajeFinal", puntaje);
            model.addAttribute("totalCategorias", categoriaService.obtenerTotal());
            model.addAttribute("nombreUsuario", nombreUsuario);
            return "categoria-final";
        }

        // Obtener pregunta
        List<ApiPregunta> preguntas = preguntaService.obtenerPreguntasPorCategoria(1, categoria.getId());

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoria.getId());
            return obtenerCategoria(cantidad, session, model);
        }

        ApiPregunta pregunta = preguntas.get(0);
        preguntaActual = pregunta;
        categoriaActualId = categoria.getId();

        // Obtener valores de sesión
        int cantidadTotal = (Integer) session.getAttribute("cantidadPreguntasTotal");
        int preguntasRespondidas = (Integer) session.getAttribute("preguntasRespondidas");

        model.addAttribute("pregunta", pregunta);
        model.addAttribute("categoria", categoria);
        model.addAttribute("categoriaId", categoria.getId());
        model.addAttribute("puntaje", puntaje);
        model.addAttribute("preguntasRespondidas", preguntasRespondidas + 1);
        model.addAttribute("cantidadPreguntasTotal", cantidadTotal);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "categoria-pregunta";
    }

    @PostMapping("/siguiente")
    public String siguientePregunta(Model model) {

        // Obtener siguiente pregunta de la categoría actual
        List<ApiPregunta> preguntas = preguntaService.obtenerPreguntasPorCategoria(1, categoriaActualId);

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoriaActualId);
            preguntaActual = null;

            model.addAttribute("puntajeFinal", puntaje);
            model.addAttribute("totalCategorias", categoriaService.obtenerTotal());
            model.addAttribute("nombreUsuario", nombreUsuario);
            return "categoria-final";
        }

        ApiPregunta pregunta = preguntas.get(0);
        preguntaActual = pregunta;

        model.addAttribute("pregunta", pregunta);
        model.addAttribute("categoria", categoriaService.obtenerPorId(categoriaActualId));
        model.addAttribute("categoriaId", categoriaActualId);
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
            HttpSession session,
            Model model) {

        if (preguntaActual == null || categoriaActualId != categoriaId) {
            return "redirect:/categoria";
        }

        // ← OBTENER VALORES DE SESIÓN
        int cantidadTotal = (Integer) session.getAttribute("cantidadPreguntasTotal");
        int preguntasRespondidas = (Integer) session.getAttribute("preguntasRespondidas");

        Categoria categoria = categoriaService.obtenerPorId(categoriaId);
        boolean acierto = respuesta.equals(preguntaActual.getRespuestaCorrectaDecodificada());

        if (acierto) {
            puntaje++;
        }

        String respuestaCorrecta = preguntaActual.getRespuestaCorrectaDecodificada();
        preguntasRespondidas++;

        // ← ACTUALIZAR en sesión
        session.setAttribute("preguntasRespondidas", preguntasRespondidas);

        // Si llegó al máximo
        if (preguntasRespondidas >= cantidadTotal) {
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

        // Obtener siguiente pregunta
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
        model.addAttribute("cantidadPreguntasTotal", cantidadTotal);
        model.addAttribute("nombreUsuario", nombreUsuario);

        if (!acierto) {
            model.addAttribute("respuestaCorrecta", respuestaCorrecta);
        }

        return "categoria-resultado";
    }

    @PostMapping("/siguiente-categoria")
    public String siguienteCategoria(
            HttpSession session,
            Model model) {

        // ← RECUPERAR cantidad de la sesión (sin parámetro)
        Integer cantidadObj = (Integer) session.getAttribute("cantidadPreguntasTotal");
        int cantidad = (cantidadObj != null) ? cantidadObj : 10;

        // Obtener siguiente categoría
        Categoria categoria = categoriaService.obtenerCategoriaRandom(categoriasUsadas);

        if (categoria == null) {
            model.addAttribute("puntajeFinal", puntaje);
            model.addAttribute("totalCategorias", categoriaService.obtenerTotal());
            model.addAttribute("nombreUsuario", nombreUsuario);
            return "categoria-final";
        }

        // Obtener pregunta
        List<ApiPregunta> preguntas = preguntaService.obtenerPreguntasPorCategoria(1, categoria.getId());

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoria.getId());
            return siguienteCategoria(session, model);
        }

        ApiPregunta pregunta = preguntas.get(0);
        preguntaActual = pregunta;
        categoriaActualId = categoria.getId();

        // Resetear contador de preguntas respondidas
        session.setAttribute("preguntasRespondidas", 0);

        model.addAttribute("pregunta", pregunta);
        model.addAttribute("categoria", categoria);
        model.addAttribute("categoriaId", categoria.getId());
        model.addAttribute("puntaje", puntaje);
        model.addAttribute("preguntasRespondidas", 1);
        model.addAttribute("cantidadPreguntasTotal", cantidad);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "categoria-pregunta";
    }

    @PostMapping("/reiniciar")
    public String reiniciar(HttpSession session) {
        categoriasUsadas.clear();
        puntaje = 0;
        preguntaActual = null;
        categoriaActualId = 0;

        // ← LIMPIAR sesión
        session.setAttribute("cantidadPreguntasTotal", null);
        session.setAttribute("preguntasRespondidas", null);

        return "redirect:/categoria";
    }
}