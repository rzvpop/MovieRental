package nucont_movieRental.controller;

import nucont_movieRental.model.Movie;
import nucont_movieRental.model.validators.MovieValidator;
import nucont_movieRental.repository.InMemoryRepository;
import nucont_movieRental.repository.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MovieControllerTest {
    private static Movie movie;
    private static MovieValidator movieValidator;
    private static Repository<Long, Movie> repository;

    private MovieController movieController;

    @Before
    public void setUp() throws Exception {
        movie = new Movie("titlu", "gen", 2010);
        movie.setId(Long.valueOf(1));
        movieValidator = new MovieValidator();
        repository = new InMemoryRepository<>(movieValidator);
        movieController = new MovieController(repository);
    }

    @After
    public void tearDown() throws Exception {
        movie = null;
        movieValidator = null;
        repository = null;
        movieController = null;
    }

    @Test
    public void testGetAllMovies() {
        Set<Movie> movies = movieController.getAllMovies();
        assertEquals("There should be no movies", movies.size(), 0);
    }

    @Test
    public void testAddMovie() {
        movieController.addMovie(movie);
        Set<Movie> movies = movieController.getAllMovies();
        assertEquals("There should be one movie", movies.size(), 1);
    }
}
