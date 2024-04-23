package com.broadcom.tanzu.demos.rambi.image;

import com.broadcom.tanzu.demos.rambi.GeneratedRambiMovie;
import com.broadcom.tanzu.demos.rambi.ImageGeneratorService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("fake")
public class FakeImageGeneratorService implements ImageGeneratorService {

    @Override
    public GeneratedRambiMovie generate(GeneratedRambiMovie movie) {
        movie.setPosterUrl("/images/fake_00.png");
        movie.setImageGenerationPrompt("```FAKE IMAGE PROMPT```");        
        movie.setRevisedImageGenerationPrompt("```No Revised Prompt```");
        return movie;
    }

}
