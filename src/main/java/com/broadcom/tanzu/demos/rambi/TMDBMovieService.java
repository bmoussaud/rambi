package com.broadcom.tanzu.demos.rambi;

import com.broadcom.tanzu.demos.rambi.image.RambiMovie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TMDBMovieService implements MovieService {

    private static final Logger logger = LoggerFactory.getLogger(TMDBMovieService.class);

    private String uriBase = "https://api.themoviedb.org/3/";
    
    @Autowired
    private RambiConfiguration configuration;

    @Override
    public RambiMovie search(String movieTitle) {

        RestClient restClient = RestClient.create();
        
        AuthenticationResponse result = restClient.get().uri(uriBase + "/authentication")
                .header("Authorization", "Bearer " + configuration.tmdb_api_key())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AuthenticationResponse.class);

        logger.info("result: " + result);

        SearchMovieResponse response = restClient.get()
                .uri(uriBase + "/search/movie?query={movieTitle}&include_adult=false&language=en-US&page=1", movieTitle)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + configuration.tmdb_api_key())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(SearchMovieResponse.class);

        logger.info("result: " + response);

        MovieResult first = response.results().get(0);
        return new RambiMovie(first.title(), first.overview(), "https://image.tmdb.org/t/p/w500" + first.poster_path());
    }

    record AuthenticationResponse(
            boolean success,
            int status_code,
            String status_message) {
    }

    record MovieResult(
            int id,
            String title,
            String overview,
            String poster_path) {
    }

    record SearchMovieResponse(
            int page,
            int total_pages,
            int total_results,
            List<MovieResult> results) {
    }

}
