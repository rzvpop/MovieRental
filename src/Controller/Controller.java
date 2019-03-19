package Controller;

import Domain.Client;
import Domain.Movie;
import Domain.Rental;
import Repo.Repo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controller {
    private Repo<Long, Movie> movie_repo;
    private Repo<Long, Client> client_repo;
    private Repo<Long, Rental> rental_repo;

    public Controller(Repo<Long, Movie> _movie_repo, Repo<Long, Client> _client_repo,
                      Repo<Long, Rental> _rental_repo)
    {
        movie_repo = _movie_repo;
        client_repo = _client_repo;
        rental_repo = _rental_repo;
    }

    public void returnMovie(long movie_id)
    {
        StreamSupport.stream(rental_repo.findAll().spliterator(), false).
                filter(r -> r.getMovie().getId() == movie_id).
                forEach(r -> {r.setReturned(true);
                            rental_repo.update(r);
                        });
    }

    public Set<Movie> mostRented()
    {
        if(rental_repo.size() > 0)
        {
            ArrayList<Rental> rentals = new ArrayList<>((Set<Rental>) rental_repo.findAll());

            Map<Long, Long> nr_rented = new HashMap<>();
            rentals.forEach(r -> nr_rented.put(r.getMovie().getId(),
                    rentals.stream().
                            filter(r_id -> r_id.getMovie().getId().equals(r.getMovie().getId())).count()));

            long max = nr_rented.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();

            Set<Map.Entry<Long, Long>> most_rented_id = nr_rented.entrySet().stream().filter(r -> r.getValue() == max)
                    .collect(Collectors.toSet());

            Set<Movie> most_rented = new HashSet<>();
            most_rented_id.forEach(r -> most_rented.add(movie_repo.findOne(r.getKey()).get()));

            return most_rented;
        }

        throw new RuntimeException("No movies rented");
    }

    public void addClient(Client client)
    {
        client_repo.save(client);
    }

    public void addRental(Rental rental)
    {
        rental_repo.save(rental);
    }

    //function to save a movie
    public void addMovie(Movie movie) {
        movie_repo.save(movie);
    }

    public Set<Movie> getAllMovies() {
        return StreamSupport.stream(movie_repo.findAll().spliterator(), false).
                collect(Collectors.toSet());
    }

    public Set<Rental> getAllRentals() {
        return StreamSupport.stream(rental_repo.findAll().spliterator(), false).
                collect(Collectors.toSet());
    }

    public Set<Client> getAllClients() {
        return StreamSupport.stream(client_repo.findAll().spliterator(), false).
                collect(Collectors.toSet());
    }

    public Set<Movie> filterMoviesByName(String str)
    {
        Set<Movie> filteredMovies = new HashSet<>();
        movie_repo.findAll().forEach(filteredMovies::add);

        return filteredMovies.stream().filter(movie -> movie.getName().contains(str)).
                collect(Collectors.toSet());
    }

    public Repo getMovieRepo()
    {
        return movie_repo;
    }

    public Repo getClientRepo()
    {
        return client_repo;
    }
}
