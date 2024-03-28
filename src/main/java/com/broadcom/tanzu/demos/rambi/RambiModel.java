package com.broadcom.tanzu.demos.rambi;

import java.util.List;

import com.broadcom.tanzu.demos.rambi.image.RambiMovie;

public class RambiModel {

    String movie1Title;
    String movie2Title;

    RambiMovie movie1;
    RambiMovie movie2;

    String genre = "Comedy";

    RambiMovie generatedMovie;

    private static final List<String> defaultGenres = List.of("Action", "Adventure", "Animation", "Comedy", "Crime",
            "Documentary", "Drama",
            "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Science Fiction", "TV Movie",
            "Thriller", "War", "Western");

    public RambiMovie getMovie1() {
        return movie1;
    }

    public void setMovie1(RambiMovie movie1) {
        this.movie1 = movie1;
    }

    public RambiMovie getMovie2() {
        return movie2;
    }

    public void setMovie2(RambiMovie movie2) {
        this.movie2 = movie2;
    }

    public RambiMovie getGeneratedMovie() {
        return generatedMovie;
    }

    public void setGeneratedMovie(RambiMovie generatedMovie) {
        this.generatedMovie = generatedMovie;
    }

    public String getMovie1Title() {
        return movie1Title;
    }

    public void setMovie1Title(String movie1Title) {
        this.movie1Title = movie1Title;
    }

    public String getMovie2Title() {
        return movie2Title;
    }

    public void setMovie2Title(String movie2Title) {
        this.movie2Title = movie2Title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getDefaultGenres() {
        return defaultGenres;
    }

    @Override
    public String toString() {
        return "RambiModel [movie1Title=" + movie1Title + ", movie2Title=" + movie2Title + ", movie1=" + movie1
                + ", movie2=" + movie2 + ", genre=" + genre + ", generatedMovie=" + generatedMovie + "]";
    }

    public static List<String> getDefaultgenres() {
        return defaultGenres;
    }

}
