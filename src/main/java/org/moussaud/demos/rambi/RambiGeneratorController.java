package org.moussaud.demos.rambi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RambiGeneratorController {

    private static final Logger logger = LoggerFactory.getLogger(RambiGeneratorController.class);
    @Autowired
    private PitchGeneratorService pitchGeneratorService;
    @Autowired
    private ImageGeneratorService imageGeneratorService;

    @PostMapping(value = "/generate/movie", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RambiMovie generate(@RequestBody RambiRequest request) {
        logger.info("generate image {}", request);
        var generated = pitchGeneratorService.generate(request.movie1(), request.movie2(), request.genre());
        var response = imageGeneratorService.generate(generated);
        logger.info("response {} ", response);
        return response;
    }
}
