package com.broadcom.tanzu.demos.rambi.dataset;

public class MovieSearch {

    String movie1Title;

    String movie2Title;

    public MovieSearch() {

    }

    public MovieSearch(String movie1Title, String movie2Title) {
        this.movie1Title = movie1Title;
        this.movie2Title = movie2Title;
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
}
