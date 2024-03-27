package com.broadcom.tanzu.demos.rambi.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageGeneratorRequest {

    private Movie generatedMovie;
    private Movie movie1;
    private Movie movie2;

    public Movie getGeneratedMovie() {
        return generatedMovie;
    }

    public void setGeneratedMovie(Movie generatedMovie) {
        this.generatedMovie = generatedMovie;
    }

    public Movie getMovie1() {
        return movie1;
    }

    public void setMovie1(Movie movie1) {
        this.movie1 = movie1;
    }

    public Movie getMovie2() {
        return movie2;
    }

    public void setMovie2(Movie movie2) {
        this.movie2 = movie2;
    }

    @Override
    public String toString() {
        return "ImageGeneratorRequest [generatedMovie=" + generatedMovie + ", movie1=" + movie1 + ", movie2=" + movie2
                + "]";
    }

}
