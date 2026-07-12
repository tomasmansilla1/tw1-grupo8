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

    private final String nombreUsuario = "tmansilla7";

    // ------- Helpers de sesión -------

    @SuppressWarnings("unchecked")
    private List<Integer> getCategoriasUsadas(HttpSession session) {
        List<Integer> categoriasUsadas = (List<Integer>) session.getAttribute("categoriasUsadas");
        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            session.setAttribute("categoriasUsadas", categoriasUsadas);
        }
        return categoriasUsadas;
    }

    private int getPuntaje(HttpSession session) {
        Integer puntaje = (Integer) session.getAttribute("puntaje");
        return puntaje != null ? puntaje : 0;
    }

    @SuppressWarnings("unchecked")
    private List<ApiPregunta> getPreguntasPendientes(HttpSession session) {
        return (List<ApiPregunta>) session.getAttribute("preguntasPendientes");
    }

    // ------- Endpoints -------

    @GetMapping
    public String inicio(HttpSession session, Model model) {
        int total = categoriaService.obtenerTotal();
        List<Integer> categoriasUsadas = getCategoriasUsadas(session);
        int restantes = total - categoriasUsadas.size();

        model.addAttribute("puntaje", getPuntaje(session));
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

        if (cantidad < 1 || cantidad > 50) {
            cantidad = 10;
        }

        // Cantidad elegida por el usuario, se reutiliza al pasar de categoría
        session.setAttribute("cantidadElegidaUsuario", cantidad);

        List<Integer> categoriasUsadas = getCategoriasUsadas(session);
        Categoria categoria = categoriaService.obtenerCategoriaRandom(categoriasUsadas);

        if (categoria == null) {
            model.addAttribute("puntajeFinal", getPuntaje(session));
            model.addAttribute("totalCategorias", categoriaService.obtenerTotal());
            model.addAttribute("nombreUsuario", nombreUsuario);
            return "categoria-final";
        }

        // Traigo TODAS las preguntas de la tanda en una sola llamada a la API
        List<ApiPregunta> preguntas = preguntaService.obtenerPreguntasPorCategoria(cantidad, categoria.getId());

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoria.getId());
            return obtenerCategoria(cantidad, session, model);
        }

        // Guardo la lista completa y el índice en sesión
        session.setAttribute("preguntasPendientes", preguntas);
        session.setAttribute("indicePreguntaActual", 0);
        session.setAttribute("categoriaActualId", categoria.getId());
        session.setAttribute("cantidadPreguntasTotal", preguntas.size());
        session.setAttribute("preguntasRespondidas", 0);

        ApiPregunta pregunta = preguntas.get(0);

        model.addAttribute("pregunta", pregunta);
        model.addAttribute("categoria", categoria);
        model.addAttribute("categoriaId", categoria.getId());
        model.addAttribute("puntaje", getPuntaje(session));
        model.addAttribute("preguntasRespondidas", 1);
        model.addAttribute("cantidadPreguntasTotal", preguntas.size());
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "categoria-pregunta";
    }

    @PostMapping("/responder")
    public String responder(
            @RequestParam String respuesta,
            @RequestParam int categoriaId,
            HttpSession session,
            Model model) {

        List<ApiPregunta> preguntasPendientes = getPreguntasPendientes(session);
        Integer indice = (Integer) session.getAttribute("indicePreguntaActual");
        Integer categoriaActualId = (Integer) session.getAttribute("categoriaActualId");
        Integer cantidadTotal = (Integer) session.getAttribute("cantidadPreguntasTotal");
        Integer preguntasRespondidas = (Integer) session.getAttribute("preguntasRespondidas");

        // Validación de estado de sesión
        if (preguntasPendientes == null || indice == null || categoriaActualId == null
                || categoriaActualId != categoriaId || indice >= preguntasPendientes.size()) {
            return "redirect:/categoria";
        }

        ApiPregunta preguntaActual = preguntasPendientes.get(indice);
        Categoria categoria = categoriaService.obtenerPorId(categoriaId);

        boolean acierto = respuesta.equals(preguntaActual.getRespuestaCorrectaDecodificada());
        String respuestaCorrecta = preguntaActual.getRespuestaCorrectaDecodificada();

        int puntaje = getPuntaje(session);
        if (acierto) {
            puntaje++;
            session.setAttribute("puntaje", puntaje);
        }

        preguntasRespondidas++;
        indice++;
        session.setAttribute("preguntasRespondidas", preguntasRespondidas);
        session.setAttribute("indicePreguntaActual", indice);

        boolean quedanPreguntas = indice < preguntasPendientes.size() && preguntasRespondidas < cantidadTotal;

        if (!quedanPreguntas) {
            // Se acabó la tanda de esta categoría
            List<Integer> categoriasUsadas = getCategoriasUsadas(session);
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

        // Sigo con la siguiente pregunta de la lista ya cargada en sesión (sin llamar a la API)
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

    @PostMapping("/siguiente")
    public String mostrarSiguientePregunta(HttpSession session, Model model) {
        List<ApiPregunta> preguntasPendientes = getPreguntasPendientes(session);
        Integer indice = (Integer) session.getAttribute("indicePreguntaActual");
        Integer categoriaActualId = (Integer) session.getAttribute("categoriaActualId");
        Integer cantidadTotal = (Integer) session.getAttribute("cantidadPreguntasTotal");
        Integer preguntasRespondidas = (Integer) session.getAttribute("preguntasRespondidas");

        if (preguntasPendientes == null || indice == null || indice >= preguntasPendientes.size()) {
            return "redirect:/categoria";
        }

        ApiPregunta pregunta = preguntasPendientes.get(indice);
        Categoria categoria = categoriaService.obtenerPorId(categoriaActualId);

        model.addAttribute("pregunta", pregunta);
        model.addAttribute("categoria", categoria);
        model.addAttribute("categoriaId", categoriaActualId);
        model.addAttribute("puntaje", getPuntaje(session));
        model.addAttribute("preguntasRespondidas", preguntasRespondidas + 1);
        model.addAttribute("cantidadPreguntasTotal", cantidadTotal);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "categoria-pregunta";
    }

    @PostMapping("/siguiente-categoria")
    public String siguienteCategoria(HttpSession session, Model model) {

        Integer cantidadObj = (Integer) session.getAttribute("cantidadElegidaUsuario");
        int cantidad = (cantidadObj != null) ? cantidadObj : 10;

        List<Integer> categoriasUsadas = getCategoriasUsadas(session);
        Categoria categoria = categoriaService.obtenerCategoriaRandom(categoriasUsadas);

        if (categoria == null) {
            model.addAttribute("puntajeFinal", getPuntaje(session));
            model.addAttribute("totalCategorias", categoriaService.obtenerTotal());
            model.addAttribute("nombreUsuario", nombreUsuario);
            return "categoria-final";
        }

        List<ApiPregunta> preguntas = preguntaService.obtenerPreguntasPorCategoria(cantidad, categoria.getId());

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoria.getId());
            return siguienteCategoria(session, model);
        }

        session.setAttribute("preguntasPendientes", preguntas);
        session.setAttribute("indicePreguntaActual", 0);
        session.setAttribute("categoriaActualId", categoria.getId());
        session.setAttribute("cantidadPreguntasTotal", preguntas.size());
        session.setAttribute("preguntasRespondidas", 0);

        ApiPregunta pregunta = preguntas.get(0);

        model.addAttribute("pregunta", pregunta);
        model.addAttribute("categoria", categoria);
        model.addAttribute("categoriaId", categoria.getId());
        model.addAttribute("puntaje", getPuntaje(session));
        model.addAttribute("preguntasRespondidas", 1);
        model.addAttribute("cantidadPreguntasTotal", preguntas.size());
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "categoria-pregunta";
    }

    @PostMapping("/reiniciar")
    public String reiniciar(HttpSession session) {
        session.invalidate();
        return "redirect:/categoria";
    }
}