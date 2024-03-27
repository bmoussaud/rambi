package com.broadcom.tanzu.demos.rambi.image;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringAIDalle2ImageGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(SpringAIDalle2ImageGeneratorService.class);

    @Autowired
    private ImageClient imageClient;

    public ImageGeneratorResponse generate(ImageGeneratorRequest request) {
        logger.info("Generate Image using AI {}", request);

        ImageOptions options = OpenAiImageOptions.builder()
                .withQuality("hd")
                .withStyle("vivid")
                .withModel("dall-e-2")
                .withN(1)
                .withHeight(512)
                .withWidth(512).build();
        logger.info("options {}", options);

        var prompt = new ImagePrompt(
                String.format("A captivating movie poster for the film %s,the plot is %s",
                        request.getGeneratedMovie().getTitle(), request.getGeneratedMovie().getPlot()),
                options);

        var response = this.imageClient.call(prompt);
        var metadata = response.getMetadata();
        logger.info("metadata created {}", metadata.created());
        ImageGeneratorResponse imageGeneratorResponse = new ImageGeneratorResponse();
        for (ImageGeneration result : response.getResults()) {
            Movie movie = new Movie();
            logger.info("Result Metadata {} ", result.getMetadata());
            logger.info("URL {}", result.getOutput().getUrl());
            movie.setPlot(request.getGeneratedMovie().getPlot());
            movie.setTitle(request.getGeneratedMovie().getTitle());
            movie.setPosterUrl(result.getOutput().getUrl());
            imageGeneratorResponse.setGeneratedMovie(movie);
        }
        return imageGeneratorResponse;
    }

}
