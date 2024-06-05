package com.broadcom.tanzu.demos.rambi.image;

import com.azure.core.exception.HttpResponseException;
import com.broadcom.tanzu.demos.rambi.GeneratedRambiMovie;
import com.broadcom.tanzu.demos.rambi.ImageGeneratorService;
import com.broadcom.tanzu.demos.rambi.configuration.RambiConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.azure.openai.AzureOpenAiImageModel;
import org.springframework.ai.azure.openai.metadata.AzureOpenAiImageGenerationMetadata;
import org.springframework.ai.chat.prompt.PromptTemplate;

import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Profile("!fake")
public class AzureOpenAIImageGeneratorService implements ImageGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(AzureOpenAIImageGeneratorService.class);

    @Autowired
    private RambiConfiguration configuration;

    @Autowired
    private AzureOpenAiImageModel imageModel;

    @Value("classpath:/movie-image-prompt-3.st")
    private Resource moviePromptRes;

    @Override
    public GeneratedRambiMovie generate(GeneratedRambiMovie movie) {
        logger.info("Generate Image using AI {}", movie);
        PromptTemplate moviePrompt = new PromptTemplate(moviePromptRes, Map.of(
                "genre", movie.getGenre().toLowerCase(),
                "title", movie.getTitle(),
                "description", movie.getPosterDescription()));
        logger.info(moviePrompt.render());
        var prompt = new ImagePrompt(moviePrompt.render());
        movie.setImageGenerationPrompt(moviePrompt.render());
        if (configuration.failfast()) {
            movie.setPosterUrl("/images/fail_fast.png");
            movie.setRevisedImageGenerationPrompt("```No Revised Prompt```");
            return movie;
        }

        try {
            var response = this.imageModel.call(prompt);
            var metadata = response.getMetadata();
            logger.info("metadata created {}", metadata.getCreated());

            for (ImageGeneration result : response.getResults()) {
                logger.info("URL {}", result.getOutput().getUrl());
                movie.setPosterUrl(result.getOutput().getUrl());
                AzureOpenAiImageGenerationMetadata imageGenerationMetadata = (AzureOpenAiImageGenerationMetadata) result
                        .getMetadata();
                var revisedPrompt = imageGenerationMetadata.getRevisedPrompt();
                logger.info("Revised Prompt {}", revisedPrompt);
                movie.setRevisedImageGenerationPrompt("```" + revisedPrompt + "```");
            }
            return movie;
        } catch (Exception e) {
            logger.error("Azure image generation error", e);
            logger.error("Azure image generation error Message [{}]", e.getMessage());
            movie.setRevisedImageGenerationPrompt("```Error:" + e.getMessage() + "```");
            movie.setPosterUrl("/images/generated_error_0.png");
            if (e instanceof HttpResponseException) {
                HttpResponseException ere = (HttpResponseException) e;
                Object o = ere.getValue();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    // Convert Map to JSON string
                    String jsonString = objectMapper.writeValueAsString(o);
                    // Deserialize JSON string to record
                    ErrorResponse errorResponse = objectMapper.readValue(jsonString, ErrorResponse.class);
                    var message = "```" + errorResponse.error.code() + ":" + errorResponse.error.message() + "```"
                            + "\n" + "\n" + "\n" + "\n" +
                            "```" + errorResponse.error.innerError.revisedPrompt() + "```";
                    var results = errorResponse.error.innerError.contentFilterResults();
                    StringBuffer sb = new StringBuffer();
                    sb.append("* hate: " + contentFilterResult2String(results.hate())).append('\n');
                    sb.append("* profanity: " + contentFilterResult2String(results.profanity())).append('\n');
                    sb.append("* self_harm: " + contentFilterResult2String(results.self_harm())).append('\n');
                    sb.append("* sexual: " + contentFilterResult2String(results.sexual())).append('\n');
                    sb.append("* violence: " + contentFilterResult2String(results.violence())).append('\n');

                    logger.error("error message {}", message);
                    movie.setRevisedImageGenerationPrompt(message + "\n" + sb.toString());

                } catch (Exception e1) {
                    logger.error("Azure image generation error Decoding Exception", e1);
                }
            }

            return movie;
        }
    }

    String contentFilterResult2String(ContentFilterResult result) {
        if (result == null) {
            return "null";
        }

        if (result.filtered()) {
            return "_" + result.filtered() + "_";
        } else {
            return "" + result.filtered();
        }

    }

    public record ContentFilterResult(
            boolean filtered,
            String severity,
            boolean detected // for profanity's "detected" field
    ) {
    }

    public record ContentFilterResults(
            ContentFilterResult hate,
            ContentFilterResult profanity,
            ContentFilterResult self_harm,
            ContentFilterResult sexual,
            ContentFilterResult violence) {
    }

    public record InnerError(
            String code,
            @JsonProperty("content_filter_results") ContentFilterResults contentFilterResults,
            @JsonProperty("revised_prompt") String revisedPrompt) {
    }

    public record Error(
            String code,
            @JsonProperty("inner_error") InnerError innerError,
            String message,
            String type) {
    }

    public record ErrorResponse(
            Error error) {
    }

}
