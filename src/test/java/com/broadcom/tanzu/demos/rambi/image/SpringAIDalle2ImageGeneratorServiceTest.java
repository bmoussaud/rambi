package com.broadcom.tanzu.demos.rambi.image;

import com.broadcom.tanzu.demos.rambi.GeneratedRambiMovie;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SpringAIDalle2ImageGeneratorServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SpringAIDalle2ImageGeneratorServiceTest.class);

    @Autowired
    AzureOpenAIImageGeneratorService service;

    @Test
    void testGenerate() {
        GeneratedRambiMovie generated = new GeneratedRambiMovie();
        generated.setTitle("Azure Dalle3 Error");
        generated.setPlot(
                "In the hilarious new comedy, Dalle is angry: it stops generating new images unless you provide delicious chocolate cookies");
        logger.info("request {}", generated);
        var response = service.generate(generated);
        logger.info("generated movie {}", response);
        assertTrue(response.getPosterUrl().startsWith("http"));
    }
}
