package org.moussaud.demos.moviegenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GeneratedRambiMovie extends RambiMovie {

    @JsonIgnore
    private String describeRefMainPoster;


    private String posterDescription;

    @JsonIgnore
    private GeneratedMovieMetadata metadata;

    public GeneratedRambiMovie() {
    }

    public GeneratedRambiMovie(String title, String plot) {
        super(title, plot);
    }

    public String getDescribeRefMainPoster() {
        return describeRefMainPoster;
    }

    public void setDescribeRefMainPoster(String describeRefMainPoster) {
        this.describeRefMainPoster = describeRefMainPoster;
    }

    public String getPosterDescription() {
        return posterDescription;
    }

    public void setPosterDescription(String posterDescription) {
        this.posterDescription = posterDescription;
    }

    public GeneratedMovieMetadata getMetadata() {
        if (metadata == null) {
            metadata = new GeneratedMovieMetadata();
        }
        return metadata;
    }

    @Override
    public String toString() {
        return "GeneratedRambiMovie{" +
                "describeRefMainPoster='" + describeRefMainPoster + '\'' +
                ", posterDescription='" + posterDescription + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}

