package Controller;

import Domain.Client;
import Domain.Movie;
import Domain.Rental;
import Repo.ClientFileRepository;
import Repo.MovieFileRepository;
import Repo.RentalFileRepository;
import Repo.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controller {
    private Repository<Long, Movie> movie_repo;
    private Repository<Long, Client> client_repo;
    private Repository<Long, Rental> rental_repo;

    public Controller(Repository<Long, Movie> _movie_repo, Repository<Long, Client> _client_repo,
                      Repository<Long, Rental> _rental_repo)
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

    public void deleteClient(long id){client_repo.delete(id);}

    public void addRental(Rental rental)
    {
        rental_repo.save(rental);
    }

    public void deleteRental(long id) { client_repo.delete(id); }

    //function to save a movie
    public void addMovie(Movie movie) {
        movie_repo.save(movie);
    }

    public void deleteMovie(long id) {movie_repo.delete(id);}

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

    public Repository<Long, Movie> getMovieRepo()
    {
        return movie_repo;
    }

    public Repository<Long, Client> getClientRepo()
    {
        return client_repo;
    }
}