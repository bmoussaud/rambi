package org.moussaud.demos.moviegenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GeneratedMovieMetadata {

    @JsonIgnore
    private String imageGenerationPrompt;

    @JsonIgnore
    private String revisedImageGenerationPrompt;

    @JsonIgnore
    private String chatServiceConfiguration;

    @JsonIgnore
    private String pitchGenerationPrompt;

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

    public String getChatServiceConfiguration() {
        return chatServiceConfiguration;
    }

    public void setChatServiceConfiguration(String chatServiceConfiguration) {
        this.chatServiceConfiguration = chatServiceConfiguration;
    }

    public String getPitchGenerationPrompt() {
        return pitchGenerationPrompt;
    }

    public void setPitchGenerationPrompt(String pitchGenerationPrompt) {
        this.pitchGenerationPrompt = pitchGenerationPrompt;
    }

    @Override
    public String toString() {
        return "GeneratedMovieMetadata{" +
                "imageGenerationPrompt='" + imageGenerationPrompt + '\'' +
                ", revisedImageGenerationPrompt='" + revisedImageGenerationPrompt + '\'' +
                ", chatServiceConfiguration='" + chatServiceConfiguration + '\'' +
                ", pitchGenerationPrompt='" + pitchGenerationPrompt + '\'' +
                '}';
    }
}


