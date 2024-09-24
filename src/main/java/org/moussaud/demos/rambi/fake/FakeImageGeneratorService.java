package org.moussaud.demos.rambi.fake;

import org.moussaud.demos.rambi.GeneratedRambiMovie;
import org.moussaud.demos.rambi.ImageGeneratorService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("fake")
public class FakeImageGeneratorService implements ImageGeneratorService {

    @Override
    public GeneratedRambiMovie generate(GeneratedRambiMovie movie) {
        movie.setPosterUrl("/images/fake_00.png");
        movie.getMetadata().setImageGenerationPrompt("```FAKE IMAGE PROMPT```");
        movie.getMetadata().setRevisedImageGenerationPrompt("```No Revised Prompt```");
        return movie;
    }

}
