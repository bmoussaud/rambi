package com.broadcom.tanzu.demos.rambi.image;

import com.broadcom.tanzu.demos.rambi.GeneratedRambiMovie;
import com.broadcom.tanzu.demos.rambi.ImageGeneratorService;
import com.broadcom.tanzu.demos.rambi.RambiConfiguration;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.azure.openai.AzureOpenAiImageOptions;
import org.springframework.ai.azure.openai.metadata.AzureOpenAiImageGenerationMetadata;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImageGeneration;

import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Profile("!fake")
public class AzureOpenAIImageGeneratorService implements ImageGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(AzureOpenAIImageGeneratorService.class);

    @Autowired
    private RambiConfiguration configuration;

    @Autowired
    private ImageClient imageClient;

    @Value("classpath:/movie-image-prompt-2.st")
    private Resource moviePromptRes;

    @Override
    public GeneratedRambiMovie generate(GeneratedRambiMovie movie) {
        logger.info("Generate Image using AI {}", movie);

        ImageOptions options = AzureOpenAiImageOptions.builder()
                // .withQuality("hd")
                .withStyle("vivid")
                .withN(1)
                .withHeight(1792)
                .withWidth(1024)
                .build();
        logger.info("options {}", options);
        PromptTemplate moviePrompt = new PromptTemplate(moviePromptRes, Map.of(
                "genre", movie.getGenre().toLowerCase(),
                "title", movie.getTitle(),
                "description", movie.getPosterDescription(),
                "plot", movie.getPlot()));
        logger.info(moviePrompt.render());
        var prompt = new ImagePrompt(moviePrompt.render(), options);
        movie.setImageGenerationPrompt(moviePrompt.render());
        if (configuration.failfast()) {
            movie.setPosterUrl("/images/fail_fast.png");
            movie.setRevisedImageGenerationPrompt("```No Revised Prompt```");
            return movie;
        }

        try {
            var response = this.imageClient.call(prompt);
            var metadata = response.getMetadata();
            logger.info("metadata created {}", metadata.created());

            for (ImageGeneration result : response.getResults()) {
                logger.info("URL {}", result.getOutput().getUrl());
                movie.setPosterUrl(result.getOutput().getUrl());
                AzureOpenAiImageGenerationMetadata imageGenerationMetadata = (AzureOpenAiImageGenerationMetadata) result
                        .getMetadata();
                var revisedPrompt = imageGenerationMetadata.getRevisedPrompt();
                logger.info("Movie {}", movie);
                logger.info("Revised Prompt {}", revisedPrompt);
                movie.setRevisedImageGenerationPrompt("```" + revisedPrompt + "```");
            }
            return movie;
        } catch (Exception e) {
            logger.error("Azure image generation error", e);
            movie.setPosterUrl("/images/generated_error_0.png");
            return movie;
        }
    }

}
