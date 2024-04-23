package com.broadcom.tanzu.demos.rambi.pitch;

import com.broadcom.tanzu.demos.rambi.RambiMovie;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest()
class PitchGeneratorServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(PitchGeneratorServiceTest.class);

    @Autowired
    AzureOpenAIPitchGeneratorService service;


    @Test
    void testGenerate() {
        RambiMovie first = new RambiMovie();
        first.setTitle("Bambi");
        first.setPlot("""
                It's spring, and all the animals of the forest are excited by the forest's latest birth, a buck fawn his mother has named Bambi. The animals are more excited than usual as Bambi's lineage means he will inherit the title of prince of the forest. Along with his mother, Bambi navigates through life with the help of his similarly aged friends, Thumper, a rabbit kit who needs to be continually reminded by his mother of all the lessons his father has taught him about how to live as a rabbit properly, and Flower, a skunk kit who likes his name. As different animals, they have their own issues and challenges which may not translate to the others. Being similarly aged, Bambi, Thumper and Flower may have to experience the uncharted phases of their lives without the knowledge or wisdom unless gleaned from those who have gone through them before. Bambi has to learn early that the lives of deer and of many of the other forest animals are not without their inherent dangers, for deer especially in the beautiful albeit exposed meadow. Bambi will also find that his ascension to prince of the forest is not a guarantee as other buck deer and situations may threaten that ascension.                                                                                                                                     
                """);

        RambiMovie second = new RambiMovie();
        second.setTitle("Rambo");
        second.setPlot("""                
                Rambo returns to the jungles of Vietnam on a mission to infiltrate an enemy base-camp and rescue the American POWs still held captive there.
                """);

        var generatedMovie = service.generate(first, second, "comedy");
        logger.info("Generated : " + generatedMovie);
    }

    @Test
    void testGenerate2() {
        RambiMovie first = new RambiMovie();
        first.setTitle("Oppenheimer");
        first.setPlot("""
                The story of J. Robert Oppenheimer's role in the development of the atomic bomb during World War II.                                                                                                                                     
                """);

        RambiMovie second = new RambiMovie();
        second.setTitle("Barbie");
        second.setPlot("""                
                Barbie and Ken are having the time of their lives in the colorful and seemingly perfect world of Barbie Land. However when they get a chance to go to the real world they soon discover the joys and perils of living among humans.
                """);

        var generatedMovie = service.generate(first, second, "comedy");
        logger.info("Generated : " + generatedMovie);
    }

    void testGenerateBO() {
        RambiMovie first = new RambiMovie();
        first.setTitle("Barbie");

        RambiMovie second = new RambiMovie();
        second.setTitle("Oppenheimer");

        var generatedMovie = service.generate(first, second, "sf");
        logger.info("Generated : " + generatedMovie);
    }

}