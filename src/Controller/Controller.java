package Controller;

import Domain.Movie;
import Repo.Repo;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controller {
    private Repo<Long, Movie> movie_repo;
    //private Repo<Long, Client> client_repo;
    //private Repo<Long, Rental> rental_repo;

    public Controller(Repo<Long, Movie> _movie_repo) {
        movie_repo = _movie_repo;
    }
	
    //function to save a movie
    public void addMovie(Movie movie) {
        movie_repo.save(movie);
    }

    public Set<Movie> getAllMovies() {
        return StreamSupport.stream(movie_repo.findAll().spliterator(), false).
                collect(Collectors.toSet());
    }

    public Set<Movie> filterMoviesByName(String str)
    {
        Set<Movie> filteredMovies = new HashSet<>();
        movie_repo.findAll().forEach(filteredMovies::add);

        return filteredMovies.stream().filter(movie -> movie.getName().contains(str)).
                collect(Collectors.toSet());
    }
}
