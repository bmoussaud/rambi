package com.broadcom.tanzu.demos.rambi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class RambiGeneratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void generate() throws Exception {

        var movie1 = new RambiMovie("Bambi", """
                It's spring, and all the animals of the forest are excited by the forest's latest birth, a buck fawn his mother has named Bambi. The animals are more excited than usual as Bambi's lineage means he will inherit the title of prince of the forest. Along with his mother, Bambi navigates through life with the help of his similarly aged friends, Thumper, a rabbit kit who needs to be continually reminded by his mother of all the lessons his father has taught him about how to live as a rabbit properly, and Flower, a skunk kit who likes his name. As different animals, they have their own issues and challenges which may not translate to the others. Being similarly aged, Bambi, Thumper and Flower may have to experience the uncharted phases of their lives without the knowledge or wisdom unless gleaned from those who have gone through them before. Bambi has to learn early that the lives of deer and of many of the other forest animals are not without their inherent dangers, for deer especially in the beautiful albeit exposed meadow. Bambi will also find that his ascension to prince of the forest is not a guarantee as other buck deer and situations may threaten that ascension.                                                                                                                                     
                """);
        var movie2 = new RambiMovie("Rambo", """                
                Rambo returns to the jungles of Vietnam on a mission to infiltrate an enemy base-camp and rescue the American POWs still held captive there.
                """);
        var request = new RambiRequest(movie1, movie2, "comedy without violence");


        this.mockMvc.perform(post("/generate/movie").accept(MediaType.APPLICATION_JSON).content(asJsonString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.title").exists()).andExpect(jsonPath("$.plot").exists()).andExpect(jsonPath("$.posterUrl").value(containsString("http")));
    }
}