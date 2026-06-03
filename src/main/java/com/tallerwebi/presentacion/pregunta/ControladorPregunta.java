package com.tallerwebi.presentacion.pregunta;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tallerwebi.config.SessionUtil;
import com.tallerwebi.dominio.pregunta.Pregunta;
import com.tallerwebi.dominio.pregunta.PreguntasService;


@Controller

@RequestMapping ("/admin")
public class ControladorPregunta {

    @Autowired
    private PreguntasService preguntaService;
    @Autowired
    private SessionUtil sessionUtil;

    // Mostrar formulario para crear pregunta
    @GetMapping ("/crearPregunta")
    public String mostrarFormulario (HttpSession session) {

        // verificar admin
        if (!sessionUtil.verificarAdmin(session) ) {
            return "redirect:/login";
        }
        return "admin/crearPregunta";
    }

    // Guardar pregunta
    @PostMapping("/crearPregunta")
    public String guardarPregunta(@ModelAttribute Pregunta pregunta, HttpSession session) {

        // verificar admin
        if (!sessionUtil.verificarAdmin(session) ) {
            return "redirect:/login";
        }
        // guardar en bd
        preguntaService.guardar(pregunta);

        // mensaje de éxito
        session.setAttribute("ok", "Pregunta creada correctamente");
        return "redirect:/admin/preguntas";
    }

    // Listar preguntas
    @GetMapping("/preguntas")
    public String listarPreguntas(HttpSession session, Model model) {

        // verificar admin
        if (!sessionUtil.verificarAdmin(session) ) {
            return "redirect:/login";
        }
        // traer preguntas de bd
        List<Pregunta> preguntas = preguntaService.listar();

        // enviar a la vista
        model.addAttribute("preguntas", preguntas);

        // mensajes de éxito o error
        model.addAttribute("ok", session.getAttribute("ok") );
        model.addAttribute("error", session.getAttribute("error") );

        // limpiar mensajes
        session.removeAttribute("ok");
        session.removeAttribute("error");
        return "admin/preguntas";
    }

    // Mostrar form editar
    @GetMapping ("/editarPregunta")
    public String editarPregunta(
        @RequestParam(required = false) Long id,
        HttpSession session, Model model) {

        // verificar admin
        if (!sessionUtil.verificarAdmin(session) ) {
            return "redirect:/login";
        }
        // validar id
        if (id == null) {
            session.setAttribute("error", "ID erroneo");
            return "redirect:/admin/preguntas";
        }

        // buscar pregunta
        Pregunta pregunta = preguntaService.obtenerPorId(id);
        // verificar si existe
        if (pregunta == null) {
            session.setAttribute("error", "Pregunta no encontrada");
            return "redirect:/admin/preguntas";
        }

        // enviar a la vista
        model.addAttribute("pregunta", pregunta);
        return "admin/editarPregunta";
    }

    // Actualizar pregunta
    @PostMapping("/editarPregunta")
    public String actualizarPregunta(@ModelAttribute Pregunta pregunta, HttpSession session) {

        // verificar admin
        if (!sessionUtil.verificarAdmin(session) ) {
            return "redirect:/login";
        }
        // actualizar en bd
        preguntaService.guardar(pregunta);

        // mensaje éxito
        session.setAttribute("ok", "Pregunta actualizada");
        return "redirect:/admin/preguntas";
    }

    // Eliminar pregunta
    @GetMapping("/eliminarPregunta")
    public String eliminarPregunta(
        @RequestParam(required = false) Long id,
        HttpSession session) {

        // verificar admin
        if (!sessionUtil.verificarAdmin(session)) {
            return "redirect:/login";
        }
        // validar id
        if (id == null) {
            session.setAttribute("error", "ID inválido");
            return "redirect:/admin/preguntas";
        }

        // eliminar pregunta
        preguntaService.eliminar(id);

        // mensaje éxito
        session.setAttribute("ok", "Pregunta eliminada correctamente");
        return "redirect:/admin/preguntas";
    }
    
}