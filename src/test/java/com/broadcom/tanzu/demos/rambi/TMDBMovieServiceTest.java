package com.broadcom.tanzu.demos.rambi;

import com.broadcom.tanzu.demos.rambi.image.RambiMovie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TMDBMovieServiceTest {

    @Autowired
    MovieService service;
    @Test
    void search() {
        RambiMovie bambi = service.search("Bambi");
    }
}