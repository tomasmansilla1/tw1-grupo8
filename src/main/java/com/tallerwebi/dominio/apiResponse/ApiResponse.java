package com.tallerwebi.dominio.apiResponse;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tallerwebi.dominio.apiPregunta.ApiPregunta;

public class ApiResponse {

    @JsonProperty("response_code")
    private int responseCode;

    private List<ApiPregunta> results;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<ApiPregunta> getResults() {
        return results;
    }

    public void setResults(List<ApiPregunta> results) {
        this.results = results;
    }
}