package com.broadcom.tanzu.demos.rambi.image;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GeneratedRambiMovie extends RambiMovie {

    @JsonIgnore
    private String describeRefMainPoster;

    @JsonIgnore
    private String pitchGenerationPrompt;

    @JsonIgnore
    private String imageGenerationPrompt;

    @JsonIgnore
    private String revisedImageGenerationPrompt;

    private String posterDescription;
    
    public GeneratedRambiMovie() {}

    public GeneratedRambiMovie(String title, String plot) {
        super(title, plot);
    }

    public String getDescribeRefMainPoster() {
        return describeRefMainPoster;
    }

    public void setDescribeRefMainPoster(String describeRefMainPoster) {
        this.describeRefMainPoster = describeRefMainPoster;
    }

    public String getPitchGenerationPrompt() {
        return pitchGenerationPrompt;
    }

    public void setPitchGenerationPrompt(String pitchGenerationPrompt) {
        this.pitchGenerationPrompt = pitchGenerationPrompt;
    }

    public String getImageGenerationPrompt() {
        return imageGenerationPrompt;
    }

    public void setImageGenerationPrompt(String imageGenerationPrompt) {
        this.imageGenerationPrompt = imageGenerationPrompt;
    }

    public String getRevisedImageGenerationPrompt() {
        return revisedImageGenerationPrompt;
    }

    public void setRevisedImageGenerationPrompt(String revisedImageGenerationPrompt) {
        this.revisedImageGenerationPrompt = revisedImageGenerationPrompt;
    }

    public String getPosterDescription() {
        return posterDescription;
    }

    public void setPosterDescription(String posterDescription) {
        this.posterDescription = posterDescription;
    }

    

}
