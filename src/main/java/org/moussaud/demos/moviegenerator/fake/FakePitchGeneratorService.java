package org.moussaud.demos.moviegenerator.fake;

import org.moussaud.demos.moviegenerator.PitchGeneratorService;
import org.moussaud.demos.moviegenerator.GeneratedRambiMovie;
import org.moussaud.demos.moviegenerator.RambiMovie;

import java.util.Map;

import org.moussaud.demos.moviegenerator.ChatClientPitchGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Profile("fake")
public class FakePitchGeneratorService implements PitchGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(ChatClientPitchGeneratorService.class);

    @Value("classpath:/movie-pitch-prompt.st")
    private Resource moviePromptRes;

    @Override
    public GeneratedRambiMovie generate(RambiMovie first, RambiMovie second, String genre) {

        BeanOutputParser<RambiMovie> parser = new BeanOutputParser<>(RambiMovie.class);
        String format = parser.getFormat();
        PromptTemplate moviePrompt = new PromptTemplate(moviePromptRes, Map.of(
                "Title1", first.getTitle(),
                "Plot1", first.getPlot(),
                "Title2", second.getTitle(),
                "Plot2", first.getPlot(),
                "genre", genre,
                "format", format));
        logger.info(moviePrompt.render());

        logger.info("generate new pitch based on move1:{} and movie2:{} with genre:{}", first, second, genre);
        var m = new GeneratedRambiMovie(
                first.getTitle() + "&" + second.getTitle(),
                "Genre:" + genre
                        + ": Bambi and his forest friends Thumper and Flower are excited for Bambi's upcoming coronation as prince of the forest. However, when a group of rowdy animals from a neighboring forest threaten to disrupt the ceremony, Bambi must turn to an unlikely ally for help: Rambo, a retired military-trained ram who has been hiding out in the forest. Together, they must train the forest animals in hilarious and unconventional ways to defend themselves against the intruders, all while navigating the challenges of growing up in the forest.");

        m.getMetadata().setPitchGenerationPrompt(moviePrompt.render());
        return m;
    }
}
