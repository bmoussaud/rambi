package com.broadcom.tanzu.demos.rambi.pitch;

import com.broadcom.tanzu.demos.rambi.PitchGeneratorService;
import com.broadcom.tanzu.demos.rambi.GeneratedRambiMovie;
import com.broadcom.tanzu.demos.rambi.RambiMovie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.autoconfigure.openai.OpenAiChatProperties;
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

    @Value("classpath:/movie-pitch-prompt-multi-modal.st")
    private Resource moviePromptRes;

    @Override
    public GeneratedRambiMovie generate(RambiMovie first, RambiMovie second, String genre) {
        logger.info("generate new pitch based on move1:{} and movie2:{} with genre:{}", first, second, genre);

        logger.info("movie 1 poster is {} ", first.getPosterUrl());
        logger.info("movie 2 poster is {} ", second.getPosterUrl());

        var movie1 = new Media(MimeTypeUtils.IMAGE_PNG, first.getPosterUrl());
        var movie2 = new Media(MimeTypeUtils.IMAGE_PNG, second.getPosterUrl());
        var media = List.of(movie1, movie2);

        var userMessage1 = new UserMessage("Explain what do you see on these two movie posters.", media);
        ChatResponse response1 = aiClient.call(new Prompt(List.of(userMessage1)));
        var o1 = response1.getResults().get(0).getOutput();
        logger.info("o1 {}", o1);

        /*
         * var userMessage2 = new
         * UserMessage("Explain what do you see on this picture of the poster of the Movie 1"
         * ,
         * List.of(new Media(MimeTypeUtils.IMAGE_PNG, second.getPosterUrl())));
         * ChatResponse response2 = aiClient.call(new Prompt(List.of(userMessage2)));
         * var o2 = response2.getResults().get(0).getOutput();
         * logger.info("o2 {}", o2);
         */

        BeanOutputParser<GeneratedRambiMovie> parser = new BeanOutputParser<>(GeneratedRambiMovie.class);
        String format = parser.getFormat();
        Map<String, Object> model = Map.of(
                "Title1", first.getTitle(),
                "Plot1", first.getPlot(),
                "Title2", second.getTitle(),
                "Plot2", second.getPlot(),
                "genre", genre,
                "format", format);

        PromptTemplate moviePrompt = new PromptTemplate(moviePromptRes, model);
        logger.info("prompt {}", moviePrompt.render());
        logger.info("media: {)", media);

        var userMessage = moviePrompt.createMessage(media);
        logger.info("UserMessage {}", userMessage);

        var response = aiClient.call(new Prompt(userMessage));
        var usage = response.getMetadata().getUsage();
        logger.info("Usage: Prompt Token:{}  Generation Token: {} Total Token:{}",
                usage.getPromptTokens(), usage.getGenerationTokens(), usage.getTotalTokens());
        logger.info(response.toString());
        AssistantMessage output = response.getResults().get(0).getOutput();
        String content = output.getContent();
        var movie = parser.parse(content);
        movie.setPitchGenerationPrompt(moviePrompt.render());
        logger.info("Movie:" + movie);
        // String describe = describeMainPoster(first);
        // logger.info("describe main poster {}", describe);
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
