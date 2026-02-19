package com.movies.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movies.exception.InvalidDataException;
import com.movies.exception.NotFoundException;
import com.movies.model.Movie;
import com.movies.repo.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository repo;

    public MovieService(MovieRepository repo) {
        this.repo = repo;
    }

    public Movie create(Movie movie) {
        if (movie == null) {
            throw new InvalidDataException("Invalid Movie null ");
        }
        return repo.save(movie);
    }

    public Movie read(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found" + id));
    }

    public Movie update(Long id, Movie update) {
        Movie movie = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        movie.setName(update.getName());
        movie.setDirector(update.getDirector());
        movie.setActors(update.getActors());

        return repo.save(movie);
    }

    public void delete(Long id) {
         if(repo.existsById(id)) {
        	 repo.deleteById(id);
         }else {
        	 throw new NotFoundException("movie not found ");

    }
    }

    public List<Movie> getAll() {
        return repo.findAll();
    }
}