package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Respuesta;
import com.tallerwebi.dominio.ServicioJuego;
import com.tallerwebi.dominio.excepcion.OpcionInvalidaException;
import com.tallerwebi.dominio.partida.Partida;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.dominio.pregunta.PreguntaDto;
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
import java.util.List;

@Controller
public class ControladorJuego {

    ServicioJuego servicioJuego;

    @Autowired
    public ControladorJuego(ServicioJuego servicioJuego) {
        this.servicioJuego = servicioJuego;
    }

    @RequestMapping(path = "/buscar-juego", method = RequestMethod.GET)
    public ModelAndView buscarJuego() {
        ModelMap model = new ModelMap();
        model.put("preguntaDto", new PreguntaDto());
        return new ModelAndView("elegir-categoria", model);
    }

    @RequestMapping(path = "/juego", method = RequestMethod.POST)
    public ModelAndView mostrarJuego(@ModelAttribute("preguntaDto") PreguntaDto preguntaDto, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        Partida partida = new Partida();

        try {
            List<Pregunta> preguntas = servicioJuego.buscarPreguntas(preguntaDto.getCategoria());
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

        request.getSession().setAttribute("partida", partida);

        model.put("partida", partida);
        return new ModelAndView("juego", model);
    }

    @RequestMapping(path = "/responder", method = RequestMethod.POST)
    public ModelAndView responder(@RequestParam("respuesta") String respuestaElegida, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        List<Pregunta> preguntas = (List<Pregunta>) request.getSession().getAttribute("preguntas");

        Integer indice = (Integer) request.getSession().getAttribute("indiceActual");

        Partida partida = (Partida) request.getSession().getAttribute("partida");

        partida.getRespuesta().getRespuestasUsuario().add(respuestaElegida);

        indice++;

        request.getSession().setAttribute("indiceActual", indice);

        if (indice >= preguntas.size()) {
            return new ModelAndView("redirect:/partida-finalizada");
        }

        model.put("pregunta", preguntas.get(indice));
        return new ModelAndView("juego", model);
    }

    @RequestMapping(path = "/partida-finalizada", method = RequestMethod.GET)
    public ModelAndView partidaFinalizada(HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Partida partida = (Partida) request.getSession().getAttribute("partida");

        List<Pregunta> preguntas = (List<Pregunta>) request.getSession().getAttribute("preguntas");

        Integer puntaje = servicioJuego.calcularPuntaje(preguntas, partida.getRespuesta());

        Boolean validacionPartida =  servicioJuego.validarPartida(puntaje);

        partida.setEsVictoria(validacionPartida);
        partida.setPuntajeObtenido(puntaje);
        model.put("puntaje", puntaje);

        servicioJuego.guardarPartida(partida);
        return new ModelAndView("puntaje-final", model);
    }
}
