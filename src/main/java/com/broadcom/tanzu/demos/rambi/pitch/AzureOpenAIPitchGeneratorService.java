package com.broadcom.tanzu.demos.rambi.pitch;

import com.broadcom.tanzu.demos.rambi.PitchGeneratorService;
import com.broadcom.tanzu.demos.rambi.image.GeneratedRambiMovie;
import com.broadcom.tanzu.demos.rambi.image.RambiMovie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;

@Service
@Profile("!fake")
public class AzureOpenAIPitchGeneratorService implements PitchGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(AzureOpenAIPitchGeneratorService.class);

    @Autowired
    private ChatClient aiClient;

    @Value("classpath:/movie-pitch-prompt.st")
    private Resource moviePromptRes;

    @Override
    public GeneratedRambiMovie generate(RambiMovie first, RambiMovie second, String genre) {
        logger.info("generate new pitch based on move1:{} and movie2:{} with genre:{}", first, second, genre);
        BeanOutputParser<GeneratedRambiMovie> parser = new BeanOutputParser<>(GeneratedRambiMovie.class);
        String format = parser.getFormat();
        PromptTemplate moviePrompt = new PromptTemplate(moviePromptRes, Map.of(
                "Title1", first.getTitle(),
                "Plot1", first.getPlot(),
                "Title2", second.getTitle(),
                "Plot2", second.getPlot(),
                "genre", genre,
                "format", format));
        logger.info(moviePrompt.render());

        var response = aiClient.call(new Prompt(moviePrompt.render()));
        var usage = response.getMetadata().getUsage();
        logger.info("Usage: Prompt Token:{}  Generation Token: {} Total Token:{}",
                usage.getPromptTokens(), usage.getGenerationTokens(), usage.getTotalTokens());
        logger.info(response.toString());
        AssistantMessage output = response.getResults().get(0).getOutput();
        String content = output.getContent();
        var movie = parser.parse(content);
        movie.setPitchGenerationPrompt(moviePrompt.render());
        logger.info("Movie:" + movie);
        //String describe = describeMainPoster(first);
        //logger.info("describe main poster {}", describe);
        return movie;
    }

    private String describeMainPoster(RambiMovie movie) {
        URI imageUrl;
        try {
            imageUrl = new java.net.URI(movie.getPosterUrl());
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(imageUrl, byte[].class);
            byte[] imageData = responseEntity.getBody();

            var userMessage = new UserMessage(
                    "Explain what do you see in this picture?", // text content
                    List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageData))); // image content

            ChatResponse response = aiClient.call(new Prompt(List.of(userMessage)));

            return response.getResult().getOutput().getContent();
        } catch (URISyntaxException e) {
            throw new RuntimeException("describeMainPoster error " + movie.getPosterUrl(), e);
        }

    }

}
