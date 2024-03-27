package com.broadcom.tanzu.demos.rambi.image;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringAIDalle2ImageGeneratorServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SpringAIDalle2ImageGeneratorServiceTest.class);

    @Autowired
    SpringAIDalle2ImageGeneratorService service;

    @Test
    void testGenerate() {

        Movie generated = new Movie();
        generated.setTitle("Rambi");
        generated.setPlot(
                "The story of a young deer growing up in the forest. The pet defends the forest with an yellow umbrella");
        ImageGeneratorRequest request = new ImageGeneratorRequest();
        logger.info("request {}", request);
        request.setGeneratedMovie(generated);
        var response = service.generate(request);
        logger.info("response {}", response);
        logger.info("movie {}", generated);
    }
}
