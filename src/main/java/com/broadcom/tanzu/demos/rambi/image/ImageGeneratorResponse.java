package com.broadcom.tanzu.demos.rambi.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageGeneratorResponse {

    private Movie generatedMovie;

    public Movie getGeneratedMovie() {
        return generatedMovie;
    }

    public void setGeneratedMovie(Movie generatedMovie) {
        this.generatedMovie = generatedMovie;
    }

    
}
