package com.broadcom.tanzu.demos.rambi.pitch;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.broadcom.tanzu.demos.rambi.GeneratedRambiMovie;
import com.broadcom.tanzu.demos.rambi.PitchGeneratorService;
import com.broadcom.tanzu.demos.rambi.RambiMovie;

@Service
@Profile("!fake")
public class AzureOpenAIPitchGeneratorService implements PitchGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(AzureOpenAIPitchGeneratorService.class);

    private final ChatClient aiClient;

    @Value("classpath:/movie-pitch-prompt-multi-modal.st")
    private Resource moviePromptRes;

    public AzureOpenAIPitchGeneratorService(@Qualifier("myChatClientProvider") ChatClient.Builder chatClientBuilder) {
        this.aiClient = chatClientBuilder.build();
    }

    record MovieDescription(
            String movie1PosterDescription,
            String movie2PosterDescription) {
    }

    @Override
    public GeneratedRambiMovie generate(RambiMovie first, RambiMovie second, String genre) {

        logger.info("generate new pitch based on move1:{} and movie2:{} with genre:{}", first, second, genre);

        logger.info("movie 1 poster is {} ", first.getPosterUrl());
        logger.info("movie 2 poster is {} ", second.getPosterUrl());

        MovieDescription moviePosterDescription = aiClient.prompt()
                .user(p -> {
                    try {
                        p.text("Explain what do you see on these two movie posters")
                                .media(MimeTypeUtils.IMAGE_PNG, new URI(first.getPosterUrl()).toURL())
                                .media(MimeTypeUtils.IMAGE_PNG, new URI(second.getPosterUrl()).toURL());
                    } catch (Exception e) {
                        throw new RuntimeException("URI error explain", e);
                    }
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

        var movie = aiClient
                .prompt()
                .user(p -> p.text(moviePromptRes).params(model)).call()
                .entity(GeneratedRambiMovie.class);

        logger.info("Movie:" + movie);
        movie.setPitchGenerationPrompt(moviePrompt.render());

        return movie;

    }

}
