package com.broadcom.tanzu.demos.rambi.image;

import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageGeneratorController {

    private static final Logger logger = LoggerFactory.getLogger(ImageGeneratorController.class);

    @Autowired
    private SpringAIDalle2ImageGeneratorService service;

    @PostMapping(value = "/generate/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageGeneratorResponse generate(@RequestBody ImageGeneratorRequest request) {
        logger.info("generate image {}", request);
        return service.generate(request);
    }

}
