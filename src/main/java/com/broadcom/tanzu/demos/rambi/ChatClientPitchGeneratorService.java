package com.broadcom.tanzu.demos.rambi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Profile("!fake")
public class ChatClientPitchGeneratorService implements PitchGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(ChatClientPitchGeneratorService.class);

    private final ChatClient chatClient;
    private final Map<String, String> configuration;
    boolean loadImage;

    @Value("classpath:/movie-pitch-prompt-multi-modal.st")
    private Resource moviePromptRes;

    public ChatClientPitchGeneratorService(@Qualifier("myChatClientProvider") ChatClient.Builder chatClientBuilder,
                                           @Qualifier("pitchServiceLoadImages") boolean loadImage,
                                           @Qualifier("pitchServiceLLMConfiguration") Map<String, String> configuration) {
        this.chatClient = chatClientBuilder.build();
        this.loadImage = loadImage;
        this.configuration = configuration;
    }

    @Override
    public GeneratedRambiMovie generate(RambiMovie first, RambiMovie second, String genre) {

        try {

            logger.info("generate new pitch based on move1:{} and movie2:{} with genre:{}", first, second, genre);

            logger.info("movie 1 poster is {} ", first.getPosterUrl());
            logger.info("movie 2 poster is {} ", second.getPosterUrl());

            MovieDescription moviePosterDescription = chatClient.prompt()
                    .user(p -> {
                        p.text("Explain what do you see on these two movie posters")
                                .media(getMedia(first.getPosterUrl()), getMedia(second.getPosterUrl()));
                    })
                    .call()
                    .entity(MovieDescription.class);

            BeanOutputConverter<GeneratedRambiMovie> parser = new BeanOutputConverter<>(GeneratedRambiMovie.class);
            String format = parser.getFormat();
            Map<String, Object> model = Map.of(
                    "Title1", first.getTitle(),
                    "Plot1", first.getPlot(),
                    "Description1", moviePosterDescription.movie1PosterDescription(),
                    "Title2", second.getTitle(),
                    "Plot2", second.getPlot(),
                    "Description2", moviePosterDescription.movie2PosterDescription(),
                    "genre", genre,
                    "format", format);

            PromptTemplate moviePrompt = new PromptTemplate(moviePromptRes, model);
            logger.info("prompt {}", moviePrompt.render());

            var movie = chatClient
                    .prompt()
                    .user(p -> p.text(moviePromptRes).params(model)).call()
                    .entity(GeneratedRambiMovie.class);


            GeneratedMovieMetadata metadata = movie.getMetadata();
            metadata.setPitchGenerationPrompt(moviePrompt.render());
            metadata.setChatServiceConfiguration(getConfiguration());

            logger.info("Pitch Movie: {}", movie);
            return movie;

        } catch (Exception e) {
            throw new RuntimeException("Pitch generator error", e);
        }

    }

    private String getConfiguration() {

        return "Configuration:\n" +
                this.configuration
                        .entrySet()
                        .stream()
                        .map(entry -> "* " + entry.getKey() + ": " + entry.getValue() + "\n")
                        .collect(Collectors.joining());
    }

    private Media getMedia(String imageUrl) {
        try {
            var url = new URI(imageUrl).toURL();
            if (loadImage) {
                logger.info("load {}...", imageUrl);
                return new Media(MimeTypeUtils.IMAGE_PNG, new UrlResource(url));
            } else {
                return new Media(MimeTypeUtils.IMAGE_PNG, url);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    record MovieDescription(
            String movie1PosterDescription,
            String movie2PosterDescription) {
    }

}
