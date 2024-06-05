package com.broadcom.tanzu.demos.rambi;

import com.broadcom.tanzu.demos.rambi.dataset.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UIController {

    private static final Logger logger = LoggerFactory.getLogger(UIController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private PitchGeneratorService pitchGeneratorService;
    @Autowired
    private ImageGeneratorService imageGeneratorService;

    @GetMapping(value = "/")
    public String index(Model model) {
        RambiModel rambiModel = new RambiModel();
        rambiModel.setMovie1Title("Barbie");
        rambiModel.setMovie2Title("Back to the Future");
        model.addAttribute("rambiModel", rambiModel);
        logger.info("rambiModel: {}", rambiModel);
        return "/views/movie-form";
    }

    @PostMapping(value = "/movie/search")
    public String movieSearch(@ModelAttribute RambiModel rambiModel, Model model) {
        logger.info("movieSearch {} & {}", rambiModel.getMovie1Title(), rambiModel.getMovie2Title());

        RambiMovie movie1 = movieService.search(rambiModel.getMovie1Title());
        RambiMovie movie2 = movieService.search(rambiModel.getMovie2Title());
        rambiModel.setMovie1(movie1);
        rambiModel.setMovie2(movie2);

        logger.info("rambiModel: {}", rambiModel);

        model.addAttribute("rambiModel", rambiModel);
        return "/fragments/query-movies-form.html";
    }

    @PostMapping(value = "/movie/aigenerate")
    public String generate(@ModelAttribute RambiModel rambiModel, Model model) {
        logger.info("AI generate image {}", rambiModel);
        var generated = pitchGeneratorService.generate(rambiModel.getMovie1(), rambiModel.getMovie2(),
                rambiModel.getGenre());
        var response = imageGeneratorService.generate(generated);
        logger.info("response {} ", response);
        rambiModel.setGeneratedMovie(response);
        model.addAttribute("rambiModel", rambiModel);
        return "/fragments/query-movies-form.html";
    }
}
