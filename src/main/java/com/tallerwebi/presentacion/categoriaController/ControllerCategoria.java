package com.tallerwebi.presentacion.categoriaController;

import com.tallerwebi.dominio.Categoria.Categoria;
import com.tallerwebi.dominio.apiPregunta.ApiPregunta;
import com.tallerwebi.dominio.categoriaDia.CategoriaHistorial;
import com.tallerwebi.dominio.categoriaDia.CategoriaService;
import com.tallerwebi.dominio.servicioCategoria.ServicioCategoria;
import com.tallerwebi.dominio.servicioPregunta.PreguntaApiService;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/categoria")
public class ControllerCategoria {

    @Autowired
    private PreguntaApiService preguntaService;

    @Autowired
    private CategoriaService categoriaDiaService;

    @Autowired
    private ServicioCategoria servicioCategoria;

    @RequestMapping(path = "", method = RequestMethod.GET)
        public ModelAndView inicio(HttpServletRequest request) {
        ModelMap model = new ModelMap();

        List<Integer> categoriasUsadas = new ArrayList<>();
        request.getSession().setAttribute("categoriasUsadas", categoriasUsadas);

        Integer puntaje = 0;
        request.getSession().setAttribute("puntaje", 0);

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        int total = servicioCategoria.obtenerTotal();
        int restantes = total - categoriasUsadas.size();

        model.put("puntaje", puntaje);
        model.put("categoriasUsadas", categoriasUsadas.size());
        model.put("totalCategorias", total);
        model.put("categoriasRestantes", restantes);
        model.put("nombreUsuario", usuario.getUsername());

        return new ModelAndView("categoria-inicio", model);
    }

    @RequestMapping(path = "/obtener", method = RequestMethod.POST)
    public ModelAndView obtenerCategoria(@RequestParam int cantidad, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        if (cantidad < 1 || cantidad > 50) {
            cantidad = 10;
        }

        // Obtener categorías usadas
        List<Integer> categoriasUsadas = (List<Integer>) request.getSession().getAttribute("categoriasUsadas");
        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            request.getSession().setAttribute("categoriasUsadas", categoriasUsadas);
        }

        // Obtener puntaje
        Integer puntaje = (Integer) request.getSession().getAttribute("puntaje");
        if (puntaje == null) {
            puntaje = 0;
            request.getSession().setAttribute("puntaje", puntaje);
        }

        // Nombre de usuario (por ahora fijo)
        Usuario nombreUsuario = (Usuario) request.getSession().getAttribute("usuario");
        if (nombreUsuario != null) {
            request.getSession().setAttribute("nombreUsuario", nombreUsuario.getUsername());
        }

        // Guardar datos de la partida
        request.getSession().setAttribute("cantidadPreguntasTotal", cantidad);
        request.getSession().setAttribute("preguntasRespondidas", 0);

        // Obtener categoría aleatoria
        CategoriaHistorial categoriaHistorial = categoriaDiaService.obtenerIdApiPregunta();

        Categoria categoria = servicioCategoria.obtenerPorId(categoriaHistorial.getApiIdNombre());

        if (categoria == null) {
            model.put("puntajeFinal", puntaje);
            model.put("totalCategorias", servicioCategoria.obtenerTotal());
            model.put("nombreUsuario", nombreUsuario);
            return new ModelAndView( "categoria-final", model);
        }

        // Obtener pregunta de la API
        List<ApiPregunta> preguntas = preguntaService.obtenerPreguntasPorCategoria(cantidad, categoria.getId());

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoria.getId());
            return obtenerCategoria(cantidad, request);
        }

        request.getSession().setAttribute("preguntas", preguntas);
        request.getSession().setAttribute("indicePregunta", 0);

        ApiPregunta pregunta = preguntas.get(0);

        // Guardar estado en sesión
        request.getSession().setAttribute("preguntaActual", pregunta);
        request.getSession().setAttribute("categoriaActualId", categoria.getId());

        Integer cantidadTotal = (Integer) request.getSession().getAttribute("cantidadPreguntasTotal");
        Integer preguntasRespondidas = (Integer) request.getSession().getAttribute("preguntasRespondidas");

        model.put("pregunta", pregunta);
        model.put("categoria", categoria);
        model.put("categoriaId", categoria.getId());
        model.put("puntaje", puntaje);
        model.put("preguntasRespondidas", preguntasRespondidas + 1);
        model.put("cantidadPreguntasTotal", cantidadTotal);
        model.put("nombreUsuario", nombreUsuario);

        return new ModelAndView( "categoria-pregunta", model);
    }

    @RequestMapping(path = "/siguiente", method = RequestMethod.POST)
    public ModelAndView siguientePregunta(HttpServletRequest request) {
        ModelMap model = new ModelMap();

        List<Integer> categoriasUsadas = (List<Integer>) request.getSession().getAttribute("categoriasUsadas");
        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            request.getSession().setAttribute("categoriasUsadas", categoriasUsadas);
        }

        Integer puntaje = (Integer) request.getSession().getAttribute("puntaje");
        if (puntaje == null) {
            puntaje = 0;
            request.getSession().setAttribute("puntaje", puntaje);
        }

        String nombreUsuario = (String) request.getSession().getAttribute("nombreUsuario");
        if (nombreUsuario == null) {
            request.getSession().setAttribute("nombreUsuario", nombreUsuario);
        }

        Integer categoriaActualId = (Integer) request.getSession().getAttribute("categoriaActualId");
        Integer preguntasRespondidas = (Integer) request.getSession().getAttribute("preguntasRespondidas");
        Integer cantidadPreguntasTotal = (Integer) request.getSession().getAttribute("cantidadPreguntasTotal");

        if (categoriaActualId == null) {
            return new ModelAndView("redirect:/categoria");
        }

        // Obtener siguiente pregunta de la categoría actual
        List<ApiPregunta> preguntas = (List<ApiPregunta>) request.getSession().getAttribute("preguntas");

        if (preguntas == null || preguntas.isEmpty()) {
            return new ModelAndView("redirect:/categoria");
        }

        Integer indice = (Integer) request.getSession().getAttribute("indicePregunta");

        indice++;

        if (indice >= preguntas.size()) {
            return new ModelAndView("redirect:/categoria");
        }

        request.getSession().setAttribute("indicePregunta", indice);

        ApiPregunta siguiente = preguntas.get(indice);

        // Guardar nueva pregunta
        request.getSession().setAttribute("preguntaActual", siguiente);

        model.put("pregunta", siguiente);
        model.put("categoria", servicioCategoria.obtenerPorId(categoriaActualId));
        model.put("categoriaId", categoriaActualId);
        model.put("puntaje", puntaje);
        model.put("preguntasRespondidas", preguntasRespondidas);
        model.put("cantidadPreguntasTotal", cantidadPreguntasTotal);
        model.put("nombreUsuario", nombreUsuario);

        return new ModelAndView("categoria-pregunta", model);
    }

    @RequestMapping(path = "/responder", method = RequestMethod.POST)
    public ModelAndView responder(@RequestParam String respuesta, @RequestParam int categoriaId,
                            HttpServletRequest request) {
        ModelMap model = new ModelMap();
        // Recuperar datos de la sesión
        ApiPregunta preguntaActual = (ApiPregunta) request.getSession().getAttribute("preguntaActual");
        Integer categoriaActualId = (Integer) request.getSession().getAttribute("categoriaActualId");
        Integer cantidadTotal = (Integer) request.getSession().getAttribute("cantidadPreguntasTotal");
        Integer preguntasRespondidas = (Integer) request.getSession().getAttribute("preguntasRespondidas");
        Integer puntaje = (Integer) request.getSession().getAttribute("puntaje");
        List<Integer> categoriasUsadas = (List<Integer>) request.getSession().getAttribute("categoriasUsadas");
        String nombreUsuario = (String) request.getSession().getAttribute("nombreUsuario");

        // Validaciones
        if (preguntaActual == null || categoriaActualId == null || categoriaActualId != categoriaId) {
            return new ModelAndView("redirect:/categoria");
        }

        if (puntaje == null) {
            puntaje = 0;
        }

        if (preguntasRespondidas == null) {
            preguntasRespondidas = 0;
        }

        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            request.getSession().setAttribute("categoriasUsadas", categoriasUsadas);
        }

        Categoria categoria = servicioCategoria.obtenerPorId(categoriaId);

        boolean acierto = respuesta.equals(
                preguntaActual.getRespuestaCorrectaDecodificada());

        if (acierto) {
            puntaje+=10;
            request.getSession().setAttribute("puntaje", puntaje);
        }

        String respuestaCorrecta =
                preguntaActual.getRespuestaCorrectaDecodificada();

        preguntasRespondidas++;
        request.getSession().setAttribute("preguntasRespondidas", preguntasRespondidas);

        // ¿Terminó la categoría?
        if (preguntasRespondidas >= cantidadTotal) {
            categoriasUsadas.add(categoriaId);
            request.getSession().setAttribute("categoriasUsadas", categoriasUsadas);
            request.getSession().removeAttribute("preguntaActual");
            request.getSession().removeAttribute("categoriaActualId");

            model.put("acierto", acierto);
            model.put("respuestaCorrecta", respuestaCorrecta);
            model.put("puntajeFinal", puntaje);
            model.put("categoria", categoria);
            model.put("nombreUsuario", nombreUsuario);
            model.put("totalCategorias", servicioCategoria.obtenerTotal());

            return new ModelAndView("categoria-final", model);
        }

        model.put("acierto", acierto);
        model.put("categoria", categoria);
        model.put("puntaje", puntaje);
        model.put("esUltimaPregunta", false);
        model.put("preguntasRespondidas", preguntasRespondidas);
        model.put("cantidadPreguntasTotal", cantidadTotal);
        model.put("nombreUsuario", nombreUsuario);

        if (!acierto) {
            model.put("respuestaCorrecta", respuestaCorrecta);
        }

        return new ModelAndView("categoria-resultado", model);
    }

    @RequestMapping(path = "/siguiente-categoria", method = RequestMethod.POST)
    public ModelAndView siguienteCategoria(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        // Recuperar datos de la sesión
        Integer cantidad = (Integer) request.getSession().getAttribute("cantidadPreguntasTotal");
        if (cantidad == null) {
            cantidad = 10;
        }

        Integer puntaje = (Integer) request.getSession().getAttribute("puntaje");
        if (puntaje == null) {
            puntaje = 0;
            request.getSession().setAttribute("puntaje", puntaje);
        }

        String nombreUsuario = (String) request.getSession().getAttribute("nombreUsuario");
        if (nombreUsuario == null) {
            nombreUsuario = "tmansilla7";
            request.getSession().setAttribute("nombreUsuario", nombreUsuario);
        }

        List<Integer> categoriasUsadas = (List<Integer>) request.getSession().getAttribute("categoriasUsadas");

        if (categoriasUsadas == null) {
            categoriasUsadas = new ArrayList<>();
            request.getSession().setAttribute("categoriasUsadas", categoriasUsadas);
        }

        // Obtener siguiente categoría
        Categoria categoria = servicioCategoria.obtenerCategoriaRandom(categoriasUsadas);

        if (categoria == null) {
            model.put("puntajeFinal", puntaje);
            model.put("totalCategorias", servicioCategoria.obtenerTotal());
            model.put("nombreUsuario", nombreUsuario);
            return new ModelAndView("categoria-final", model);
        }

        // Obtener pregunta
        List<ApiPregunta> preguntas =
                preguntaService.obtenerPreguntasPorCategoria(cantidad, categoria.getId());

        if (preguntas.isEmpty()) {
            categoriasUsadas.add(categoria.getId());
            return siguienteCategoria(request);
        }

        request.getSession().setAttribute("preguntas", preguntas);

        request.getSession().setAttribute("indicePregunta", 0);

        ApiPregunta pregunta = preguntas.get(0);

        // Guardar estado de la nueva categoría
        request.getSession().setAttribute("preguntaActual", pregunta);
        request.getSession().setAttribute("categoriaActualId", categoria.getId());
        request.getSession().setAttribute("preguntasRespondidas", 0);

        model.put("pregunta", pregunta);
        model.put("categoria", categoria);
        model.put("categoriaId", categoria.getId());
        model.put("puntaje", puntaje);
        model.put("preguntasRespondidas", 1);
        model.put("cantidadPreguntasTotal", cantidad);
        model.put("nombreUsuario", nombreUsuario);

        return new ModelAndView("categoria-pregunta", model);
    }

    @RequestMapping(path = "/reiniciar", method = RequestMethod.POST)
    public ModelAndView reiniciar(HttpServletRequest request) {

        request.getSession().removeAttribute("categoriasUsadas");
        request.getSession().removeAttribute("puntaje");
        request.getSession().removeAttribute("preguntaActual");
        request.getSession().removeAttribute("categoriaActualId");
        request.getSession().removeAttribute("cantidadPreguntasTotal");
        request.getSession().removeAttribute("preguntasRespondidas");
        request.getSession().removeAttribute("nombreUsuario");

        return new ModelAndView("redirect:/categoria");
    }
}