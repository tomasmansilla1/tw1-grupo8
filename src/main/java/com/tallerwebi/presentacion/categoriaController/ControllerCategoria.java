package com.tallerwebi.presentacion.categoriaController;

import com.tallerwebi.dominio.Categoria.Categoria;
import com.tallerwebi.dominio.apiPregunta.ApiPregunta;
import com.tallerwebi.dominio.servicioCategoria.ServicioCategoria;
import com.tallerwebi.dominio.servicioPregunta.PreguntaApiService;
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
    private PreguntaApiService preguntaService;

    @Autowired
    private ServicioCategoria categoriaService;

    @GetMapping
    public String inicio(HttpSession session, Model model) {
        List<Integer> categoriasUsadas = (List<Integer>) session.getAttribute("categoriasUsadas");

        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            session.setAttribute("categoriasUsadas", categoriasUsadas);
        }

        Integer puntaje = (Integer) session.getAttribute("puntaje");
        if (puntaje == null) {
            puntaje = 0;
            session.setAttribute("puntaje", puntaje);
        }

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        if (nombreUsuario == null) {
            nombreUsuario = "tmansilla7";
            session.setAttribute("nombreUsuario", nombreUsuario);
        }

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
    public String obtenerCategoria(@RequestParam int cantidad, HttpSession session, Model model) {
        // Validar cantidad
        if (cantidad < 1 || cantidad > 50) {
            cantidad = 10;
        }

        // Obtener categorías usadas
        List<Integer> categoriasUsadas = (List<Integer>) session.getAttribute("categoriasUsadas");
        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            session.setAttribute("categoriasUsadas", categoriasUsadas);
        }

        // Obtener puntaje
        Integer puntaje = (Integer) session.getAttribute("puntaje");
        if (puntaje == null) {
            puntaje = 0;
            session.setAttribute("puntaje", puntaje);
        }

        // Nombre de usuario (por ahora fijo)
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        if (nombreUsuario == null) {
            nombreUsuario = "tmansilla7";
            session.setAttribute("nombreUsuario", nombreUsuario);
        }

        // Guardar datos de la partida
        session.setAttribute("cantidadPreguntasTotal", cantidad);
        session.setAttribute("preguntasRespondidas", 0);

        // Obtener categoría aleatoria
        Categoria categoria = categoriaService.obtenerCategoriaRandom(categoriasUsadas);

        if (categoria == null) {
            model.addAttribute("puntajeFinal", puntaje);
            model.addAttribute("totalCategorias", categoriaService.obtenerTotal());
            model.addAttribute("nombreUsuario", nombreUsuario);
            return "categoria-final";
        }

        // Obtener pregunta de la API
        List<ApiPregunta> preguntas = preguntaService.obtenerPreguntasPorCategoria(cantidad, categoria.getId());

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoria.getId());
            return obtenerCategoria(cantidad, session, model);
        }

        session.setAttribute("preguntas", preguntas);
        session.setAttribute("indicePregunta", 0);

        ApiPregunta pregunta = preguntas.get(0);

        // Guardar estado en sesión
        session.setAttribute("preguntaActual", pregunta);
        session.setAttribute("categoriaActualId", categoria.getId());

        Integer cantidadTotal = (Integer) session.getAttribute("cantidadPreguntasTotal");
        Integer preguntasRespondidas = (Integer) session.getAttribute("preguntasRespondidas");

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
    public String siguientePregunta(HttpSession session, Model model) {

        List<Integer> categoriasUsadas = (List<Integer>) session.getAttribute("categoriasUsadas");
        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            session.setAttribute("categoriasUsadas", categoriasUsadas);
        }

        Integer puntaje = (Integer) session.getAttribute("puntaje");
        if (puntaje == null) {
            puntaje = 0;
            session.setAttribute("puntaje", puntaje);
        }

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        if (nombreUsuario == null) {
            nombreUsuario = "tmansilla7";
            session.setAttribute("nombreUsuario", nombreUsuario);
        }

        Integer categoriaActualId = (Integer) session.getAttribute("categoriaActualId");
        Integer preguntasRespondidas = (Integer) session.getAttribute("preguntasRespondidas");
        Integer cantidadPreguntasTotal = (Integer) session.getAttribute("cantidadPreguntasTotal");

        if (categoriaActualId == null) {
            return "redirect:/categoria";
        }

        // Obtener siguiente pregunta de la categoría actual
        List<ApiPregunta> preguntas = (List<ApiPregunta>) session.getAttribute("preguntas");

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoriaActualId);
            session.removeAttribute("preguntaActual");
            session.removeAttribute("categoriaActualId");

            model.addAttribute("puntajeFinal", puntaje);
            model.addAttribute("totalCategorias", categoriaService.obtenerTotal());
            model.addAttribute("nombreUsuario", nombreUsuario);

            return "categoria-final";
        }

        Integer indice = (Integer) session.getAttribute("indicePregunta");

        indice++;

        session.setAttribute("indicePregunta", indice);

        ApiPregunta siguiente = preguntas.get(indice);

        // Guardar nueva pregunta
        session.setAttribute("preguntaActual", siguiente);

        model.addAttribute("pregunta", siguiente);
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

        // Recuperar datos de la sesión
        ApiPregunta preguntaActual = (ApiPregunta) session.getAttribute("preguntaActual");
        Integer categoriaActualId = (Integer) session.getAttribute("categoriaActualId");
        Integer cantidadTotal = (Integer) session.getAttribute("cantidadPreguntasTotal");
        Integer preguntasRespondidas = (Integer) session.getAttribute("preguntasRespondidas");
        Integer puntaje = (Integer) session.getAttribute("puntaje");
        List<Integer> categoriasUsadas =
                (List<Integer>) session.getAttribute("categoriasUsadas");
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        // Validaciones
        if (preguntaActual == null || categoriaActualId == null || categoriaActualId != categoriaId) {
            return "redirect:/categoria";
        }

        if (puntaje == null) {
            puntaje = 0;
        }

        if (preguntasRespondidas == null) {
            preguntasRespondidas = 0;
        }

        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            session.setAttribute("categoriasUsadas", categoriasUsadas);
        }

        Categoria categoria = categoriaService.obtenerPorId(categoriaId);

        boolean acierto = respuesta.equals(
                preguntaActual.getRespuestaCorrectaDecodificada());

        if (acierto) {
            puntaje++;
            session.setAttribute("puntaje", puntaje);
        }

        String respuestaCorrecta =
                preguntaActual.getRespuestaCorrectaDecodificada();

        preguntasRespondidas++;
        session.setAttribute("preguntasRespondidas", preguntasRespondidas);

        // ¿Terminó la categoría?
        if (preguntasRespondidas >= cantidadTotal) {

            categoriasUsadas.add(categoriaId);

            session.removeAttribute("preguntaActual");
            session.removeAttribute("categoriaActualId");

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
        List<ApiPregunta> siguientes =
                preguntaService.obtenerPreguntasPorCategoria(1, categoriaId);

        if (siguientes.isEmpty()) {

            categoriasUsadas.add(categoriaId);

            session.removeAttribute("preguntaActual");
            session.removeAttribute("categoriaActualId");

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

        // Guardar la siguiente pregunta en sesión
        session.setAttribute("preguntaActual", siguiente);

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
    public String siguienteCategoria(HttpSession session, Model model) {

        // Recuperar datos de la sesión
        Integer cantidad = (Integer) session.getAttribute("cantidadPreguntasTotal");
        if (cantidad == null) {
            cantidad = 10;
        }

        Integer puntaje = (Integer) session.getAttribute("puntaje");
        if (puntaje == null) {
            puntaje = 0;
            session.setAttribute("puntaje", puntaje);
        }

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        if (nombreUsuario == null) {
            nombreUsuario = "tmansilla7";
            session.setAttribute("nombreUsuario", nombreUsuario);
        }

        List<Integer> categoriasUsadas =
                (List<Integer>) session.getAttribute("categoriasUsadas");

        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            session.setAttribute("categoriasUsadas", categoriasUsadas);
        }

        // Obtener siguiente categoría
        Categoria categoria = categoriaService.obtenerCategoriaRandom(categoriasUsadas);

        if (categoria == null) {
            model.addAttribute("puntajeFinal", puntaje);
            model.addAttribute("totalCategorias", categoriaService.obtenerTotal());
            model.addAttribute("nombreUsuario", nombreUsuario);
            return "categoria-final";
        }

        // Obtener pregunta
        List<ApiPregunta> preguntas =
                preguntaService.obtenerPreguntasPorCategoria(cantidad, categoria.getId());

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoria.getId());
            return siguienteCategoria(session, model);
        }

        session.setAttribute("preguntas", preguntas);

        session.setAttribute("indicePregunta", 0);

        ApiPregunta pregunta = preguntas.get(0);

        // Guardar estado de la nueva categoría
        session.setAttribute("preguntaActual", pregunta);
        session.setAttribute("categoriaActualId", categoria.getId());
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

        session.removeAttribute("categoriasUsadas");
        session.removeAttribute("puntaje");
        session.removeAttribute("preguntaActual");
        session.removeAttribute("categoriaActualId");
        session.removeAttribute("cantidadPreguntasTotal");
        session.removeAttribute("preguntasRespondidas");
        session.removeAttribute("nombreUsuario");

        return "redirect:/categoria";
    }
}