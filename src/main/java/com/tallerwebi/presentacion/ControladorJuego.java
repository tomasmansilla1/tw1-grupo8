package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.categoriaDia.CategoriaService;
import com.tallerwebi.dominio.excepcion.OpcionInvalidaException;
import com.tallerwebi.dominio.juego.Respuesta;
import com.tallerwebi.dominio.juego.ServicioJuego;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.dominio.pregunta.PreguntaService;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
public class ControladorJuego {

    ServicioJuego servicioJuego;
    private CategoriaService categoriaService;
    private RepositoryUsuario repositoryUsuario;
    private PreguntaService preguntaService;

    @Autowired
    public ControladorJuego(
        ServicioJuego servicioJuego,
        PreguntaService preguntaService,
        CategoriaService categoriaService,
        RepositoryUsuario repositoryUsuario) 
    {
        this.servicioJuego = servicioJuego;
        this.preguntaService = preguntaService;
        this.categoriaService = categoriaService;
        this.repositoryUsuario = repositoryUsuario;
    }

    @RequestMapping(path = "/buscar-juego", method = RequestMethod.GET)
    public ModelAndView buscarJuego() {

        ModelMap model = new ModelMap();
        model.put("categoriaActual", categoriaService.obtenerCategoriaActiva());

        return new ModelAndView("categoria-del-dia", model);
    }

    @RequestMapping(path = "/tutorial", method = RequestMethod.GET)
    public ModelAndView irATutorial() {
        return new ModelAndView("tutorial");
    }
    @RequestMapping(path = "/como-jugar", method = RequestMethod.GET)
    public ModelAndView comoJugar() {
        return new ModelAndView("simulador-juego");
    }

    @RequestMapping(path = "/juegos", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView mostrarJuego(@ModelAttribute("pregunta") Pregunta pregunta, HttpServletRequest request) {
       
        ModelMap model = new ModelMap();
        Partida partida = new Partida();

        try {
            String categoriaActual = categoriaService.obtenerCategoriaActiva();
            List<Pregunta> preguntas = preguntaService.obtenerPorCategoria(categoriaActual);
            
            if (preguntas.isEmpty()) {
                model.put(
                    "error",
                    "No existen preguntas para la categoría del día"
                );
                return new ModelAndView("home", model); 
            }
            Collections.shuffle(preguntas);

            model.put("cantidadPreguntas",preguntas.size());
            model.put("categoriaActual",categoriaActual);

            Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
            
            if (usuario != null) {
                model.put("racha", usuario.getPartidasGanadasSeguidas());
                model.put("puntaje", usuario.getPuntaje());
            }

            request.getSession().setAttribute("preguntas", preguntas);
            model.put("pregunta", preguntas.get(0));
            
        } catch (OpcionInvalidaException e) {

            model.put("error", e.getMessage());
            return new ModelAndView("redirect:/buscar-juego", model);
        }

        request.getSession().setAttribute("indiceActual", 0);
        Respuesta respuesta = new Respuesta();
        partida.setFecha(LocalDateTime.now());
        partida.setRespuesta(respuesta);

        Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
        partida.setUsuario(usuario);

        request.getSession().setAttribute("partida", partida);
        model.put("partida", partida);

        return new ModelAndView("juego", model);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/responder", method = RequestMethod.POST)
    public ModelAndView responder(@RequestParam("respuesta") String respuestaElegida, HttpServletRequest request) {
       
        ModelMap model = new ModelMap();
        List<Pregunta> preguntas = (List<Pregunta>) request.getSession().getAttribute("preguntas");
        Integer indice = (Integer) request.getSession().getAttribute("indiceActual");
        Partida partida = (Partida) request.getSession().getAttribute("partida");

        Pregunta preguntaActual = preguntas.get(indice);
        boolean correcta = preguntaActual.getCorrecta().equalsIgnoreCase(respuestaElegida);

        request.getSession().setAttribute("ultimaRespuestaCorrecta",correcta);

        partida.getRespuesta().getRespuestasUsuario().add(respuestaElegida);
        indice++;
        request.getSession().setAttribute("indiceActual", indice);

        if (indice >= preguntas.size()) {
            return new ModelAndView("redirect:/partida-finalizada");
        }

        model.put("pregunta", preguntas.get(indice));
        model.put("cantidadPreguntas",preguntas.size());
        model.put("categoriaActual",categoriaService.obtenerCategoriaActiva());

        Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");

        if (usuario != null) {
            model.put("racha", usuario.getPartidasGanadasSeguidas());
            model.put("puntaje", usuario.getPuntaje());
        }

        return new ModelAndView("juego", model);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/partida-finalizada", method = RequestMethod.GET)
    public ModelAndView partidaFinalizada(HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Partida partida = (Partida) request.getSession().getAttribute("partida");
        List<Pregunta> preguntas = (List<Pregunta>) request.getSession().getAttribute("preguntas");
        Usuario usuarioNuevo = (Usuario)request.getSession().getAttribute("usuario");

        Integer puntaje = servicioJuego.calcularPuntaje(preguntas, partida.getRespuesta());
        Boolean validacionPartida = servicioJuego.validarPartida(puntaje);
        
        if (usuarioNuevo != null) {
            if (validacionPartida) {
                usuarioNuevo.setPartidasGanadasSeguidas(
                usuarioNuevo.getPartidasGanadasSeguidas() + 1);
            } else {
                usuarioNuevo.setPartidasGanadasSeguidas(0);
            }
            usuarioNuevo.setPuntaje(usuarioNuevo.getPuntaje() + puntaje);
            repositoryUsuario.modificar(usuarioNuevo);
        }

        partida.setEsVictoria(validacionPartida);
        partida.setPuntajeObtenido(puntaje);
        model.put("puntaje", puntaje);

        if (usuarioNuevo != null) {
            usuarioNuevo.sumarPuntos(puntaje);
            repositoryUsuario.modificar(usuarioNuevo);
        }

        servicioJuego.guardarPartida(partida);

        request.getSession().removeAttribute("preguntas");
        request.getSession().removeAttribute("partida");
        request.getSession().removeAttribute("indiceActual");
        request.getSession().removeAttribute("ultimaRespuestaCorrecta");

        return new ModelAndView("puntaje-final", model);
    }
}