package com.tallerwebi.dominio.servicioPregunta;

import com.tallerwebi.dominio.apiPregunta.ApiPregunta;
import com.tallerwebi.dominio.apiResponse.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class PreguntaService {

    private final RestTemplate restTemplate;
    private final String API_URL = "https://opentdb.com/api.php";
    private final String TRANSLATE_API = "https://api.mymemory.translated.net/get";

    private static final Map<String, String> cacheTraduccion = new java.util.HashMap<>();

    public PreguntaService() {
        this.restTemplate = new RestTemplate();
    }

    public List<ApiPregunta> obtenerPreguntas(int cantidad) {
        try {
            String url = API_URL + "?amount=" + cantidad;
            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);

            if (response != null && response.getResponseCode() == 0) {
                for (ApiPregunta pregunta : response.getResults()) {
                    traducirPregunta(pregunta);
                }
                return response.getResults();
            } else {
                throw new RuntimeException("Error en la API: código " +
                        (response != null ? response.getResponseCode() : "null"));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener preguntas: " + e.getMessage());
            throw new RuntimeException("No se pudieron obtener las preguntas", e);
        }
    }

    public List<ApiPregunta> obtenerPreguntasPorDificultad(int cantidad, String dificultad) {
        try {
            String url = API_URL + "?amount=" + cantidad + "&difficulty=" + dificultad.toLowerCase();

            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);

            if (response != null && response.getResponseCode() == 0) {
                for (ApiPregunta pregunta : response.getResults()) {
                    traducirPregunta(pregunta);
                }
                return response.getResults();
            } else {
                throw new RuntimeException("Error en la API");
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudieron obtener las preguntas", e);
        }
    }

    public List<ApiPregunta> obtenerPreguntasPorCategoria(int cantidad, int categoria) {
        try {
            String url = API_URL + "?amount=" + cantidad + "&category=" + categoria;
            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);

            if (response != null && response.getResponseCode() == 0) {
                for (ApiPregunta pregunta : response.getResults()) {
                    traducirPregunta(pregunta);
                }
                return response.getResults();
            } else {
                throw new RuntimeException("Error en la API");
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudieron obtener las preguntas", e);
        }
    }

    private void traducirPregunta(ApiPregunta pregunta) {
        String preguntaOriginal = pregunta.getQuestion();
        String preguntaTraducida = traducir(preguntaOriginal);
        pregunta.setQuestion(preguntaTraducida);

        String respuestaOriginal = pregunta.getCorrectAnswer();
        String respuestaTraducida = traducir(respuestaOriginal);
        pregunta.setCorrectAnswer(respuestaTraducida);

        if (pregunta.getIncorrectAnswers() != null) {
            for (int i = 0; i < pregunta.getIncorrectAnswers().size(); i++) {
                String respuestaIncorrectaOriginal = pregunta.getIncorrectAnswers().get(i);
                String respuestaIncorrectaTraducida = traducir(respuestaIncorrectaOriginal);
                pregunta.getIncorrectAnswers().set(i, respuestaIncorrectaTraducida);
            }
        }
    }

    private String traducir(String texto) {
        try {
            texto = decodeHtml(texto);

            if (cacheTraduccion.containsKey(texto)) {
                return cacheTraduccion.get(texto);
            }

            String url = TRANSLATE_API + "?q=" + java.net.URLEncoder.encode(texto, "UTF-8")
                    + "&langpair=en|es";

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("responseData")) {
                Map<String, Object> responseData = (Map<String, Object>) response.get("responseData");
                String textoTraducido = (String) responseData.get("translatedText");

                cacheTraduccion.put(texto, textoTraducido);

                return textoTraducido;
            }

            return texto;

        } catch (Exception e) {
            System.err.println("Error al traducir: " + e.getMessage());
            // Si hay error, devolver el texto original
            return texto;
        }
    }

    private String decodeHtml(String html) {
        return html.replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#039;", "'")
                .replace("&rsquo;", "'");
    }
}