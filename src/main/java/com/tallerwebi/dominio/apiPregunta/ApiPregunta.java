package com.tallerwebi.dominio.apiPregunta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiPregunta {
    private String type;
    private String difficulty;
    private String category;
    private String question;

    @JsonProperty("correct_answer")
    private String correctAnswer;

    @JsonProperty("incorrect_answers")
    private List<String> incorrectAnswers;

    public ApiPregunta() {}

    public List<String> getTodasLasOpciones() {
        List<String> opciones = new ArrayList<>();
        opciones.add(this.correctAnswer);
        opciones.addAll(this.incorrectAnswers);
        Collections.shuffle(opciones); // Mezcla las opciones
        return opciones;
    }

    public String getPreguntaDecodificada() {
        String texto = decodeUrlEncoding(this.question);  // ← AGREGAR ESTO
        return decodeHtml(texto);
    }

    public String getRespuestaCorrectaDecodificada() {
        String texto = decodeUrlEncoding(this.correctAnswer);  // ← AGREGAR ESTO
        return decodeHtml(texto);
    }

    public List<String> getOpcionesDecodificadas() {
        List<String> opciones = new ArrayList<>();
        for (String opcion : getTodasLasOpciones()) {
            String textoDecodificado = decodeUrlEncoding(opcion);  // ← AGREGAR ESTO
            opciones.add(decodeHtml(textoDecodificado));
        }
        return opciones;
    }

    private String decodeHtml(String html) {
        try {
            html = java.net.URLDecoder.decode(html, "UTF-8");
        } catch (Exception e) {

        }

        return html.replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#039;", "'")
                .replace("&rsquo;", "'");
    }

    // Getters y Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        switch(this.difficulty.toLowerCase()) {
            case "easy":
                return "Fácil";
            case "medium":
                return "Medio";
            case "hard":
                return "Difícil";
            default:
                return this.difficulty;
        }
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    private String decodeUrlEncoding(String text) {
        try {
            return java.net.URLDecoder.decode(text, "UTF-8");
        } catch (Exception e) {
            return text;
        }
    }
}
