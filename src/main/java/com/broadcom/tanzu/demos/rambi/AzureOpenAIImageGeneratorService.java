package com.broadcom.tanzu.demos.rambi;

import com.azure.core.exception.HttpResponseException;
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
        logger.info("Generate Image using AI");
        GeneratedMovieMetadata generatedMovieMetadata = movie.getMetadata();

        PromptTemplate moviePrompt = new PromptTemplate(moviePromptRes, Map.of(
                "genre", movie.getGenre().toLowerCase(),
                "title", movie.getTitle(),
                "description", movie.getPosterDescription()));
        logger.info("prompt is {}", moviePrompt.render());
        var prompt = new ImagePrompt(moviePrompt.render());
        generatedMovieMetadata.setImageGenerationPrompt(moviePrompt.render());
        if (configuration.failfast()) {
            movie.setPosterUrl("/images/fail_fast.png");
            generatedMovieMetadata.setRevisedImageGenerationPrompt("```No Revised Prompt```");
            logger.info("GenImage Movie FF: {}", movie);
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
                generatedMovieMetadata.setRevisedImageGenerationPrompt("```" + revisedPrompt + "```");
            }
            return movie;
        } catch (Exception e) {
            logger.error("Azure image generation error", e);
            logger.error("Azure image generation error Message [{}]", e.getMessage());
            generatedMovieMetadata.setRevisedImageGenerationPrompt("```Error:" + e.getMessage() + "```");
            movie.setPosterUrl("/images/generated_error_0.png");
            if (e instanceof HttpResponseException) {
                HttpResponseException httpResponseException = (HttpResponseException) e;
                Object errorValue = httpResponseException.getValue();
                ObjectMapper objectMapper = new ObjectMapper();
                
                try {
                    // Convert Map to JSON string
                    String jsonString = objectMapper.writeValueAsString(errorValue);
                    // Deserialize JSON string to ErrorResponse object
                    ErrorResponse errorResponse = objectMapper.readValue(jsonString, ErrorResponse.class);
                
                    String message = "```" + errorResponse.error.code() + ":" + errorResponse.error.message() + "```" + "\n\n\n\n" + "```";
                    StringBuilder sb = new StringBuilder();
                
                    if (errorResponse.error.innerError != null) {
                        message += " " + errorResponse.error.innerError.revisedPrompt();
                        var results = errorResponse.error.innerError.contentFilterResults();
                
                        sb.append("* hate: ").append(contentFilterResult2String(results.hate())).append('\n');
                        sb.append("* profanity: ").append(contentFilterResult2String(results.profanity())).append('\n');
                        sb.append("* self_harm: ").append(contentFilterResult2String(results.self_harm())).append('\n');
                        sb.append("* sexual: ").append(contentFilterResult2String(results.sexual())).append('\n');
                        sb.append("* violence: ").append(contentFilterResult2String(results.violence())).append('\n');
                        sb.append("* jailbreak: ").append(contentFilterResult2String(results.jailbreak())).append('\n');
                    }
                
                    logger.error("error message {}", message);
                    generatedMovieMetadata.setRevisedImageGenerationPrompt(message + "\n" + sb.toString());
                
                } catch (Exception ex) {
                    logger.error("Azure image generation error Decoding Exception", ex);
                }
            }
            logger.info("GenImage Movie: {}", movie);
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
            //String severity,
            boolean detected // for profanity's "detected" field
    ) {
    }

    public record ContentFilterResults(
            ContentFilterResult hate,
            ContentFilterResult profanity,
            ContentFilterResult self_harm,
            ContentFilterResult sexual,
            ContentFilterResult violence,
            ContentFilterResult jailbreak) {
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
