package com.broadcom.tanzu.demos.rambi.image;

public class RambiMovie {

    private String genre;
    private String title;
    private String plot;
    private String posterUrl;

    public RambiMovie() {
    }

    public RambiMovie(String title, String plot) {
        this(title, plot, null);
    }

    public RambiMovie(String title, String plot, String posterUrl) {
        this.title = title;
        this.plot = plot;
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "RambiMovie [genre=" + genre + ", title=" + title + ", plot=" + plot + ", posterUrl=" + posterUrl + "]";
    }

}
