package com.movies;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.Movie;
import com.movies.repo.MovieRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MoviesApplicationTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MovieRepository movieRepository;

    @BeforeEach
    void cleanUp() {
        movieRepository.deleteAllInBatch();
    }

    @Test
    void giveMovie_whenCreateMovie_thenReturnMovie() throws Exception {

        Movie movie = new Movie();
        movie.setName("bhahubali");
        movie.setDirector("ss RajaMouli");
        movie.setActors(List.of("Prabhas","NTR","Alliabhat"));

        mvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect(jsonPath("$.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors")
                        .value(containsInAnyOrder("Prabhas","NTR","Alliabhat")));
    }

    @Test
    void giveMovie_whenFetchMovie_thenReturnMovie() throws Exception {

        Movie movie = new Movie();
        movie.setName("bhahubali");
        movie.setDirector("ss RajaMouli");
        movie.setActors(List.of("Prabhas","NTR","Alliabhat"));

        Movie saved = movieRepository.saveAndFlush(movie);

        mvc.perform(get("/movies/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(movie.getName())));
    }

    @Test
    void giveSavedMovie_whenUpdateMovie_thenMovieUpdatedInDb() throws Exception {

        Movie movie = new Movie();
        movie.setName("bhahubali");
        movie.setDirector("ss RajaMouli");
        movie.setActors(List.of("Prabhas","NTR","Alliabhat"));
        

        Movie saved = movieRepository.saveAndFlush(movie);
        
        movie.setActors(List.of("Prabhas","NTR","Alliabhat","Ajaydevgan"));
        
        mvc.perform(put("/movies/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk());

        mvc.perform(get("/movies/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actors")
                        .value(containsInAnyOrder("Prabhas","NTR","Alliabhat","Ajaydevgan")));
    }

    @Test
    void giveDeleteMovie_whenDeleteMovie_thenMovieDeleteInDb() throws Exception {

        Movie movie = new Movie();
        movie.setName("bhahubali");
        movie.setDirector("ss RajaMouli");
        movie.setActors(List.of("Prabhas","NTR","Alliabhat"));

        Movie saved = movieRepository.saveAndFlush(movie);

        mvc.perform(delete("/movies/{id}", saved.getId()))
                .andExpect(status().isOk());

        assertFalse(movieRepository.findById(saved.getId()).isPresent());
    }
}