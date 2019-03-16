package Controller;

import Domain.Client;
import Domain.Movie;
import Domain.Rental;
import Domain.RentalComparatorByMovie;
import Repo.Repo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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

    public void returnMovie(long rental_id)
    {
        Optional<Rental> rental = rental_repo.findOne(rental_id);

        rental.ifPresent(rental1 -> rental1.setReturned(true));
        rental.ifPresent(rental1 -> rental_repo.update(rental1));
    }

    public Set<Movie> mostRented()
    {
        if(rental_repo.size() > 0)
        {
            Set<Rental> all = (Set<Rental>) rental_repo.findAll();
            ArrayList<Rental> rentals = new ArrayList<>(all);
            RentalComparatorByMovie cmp = new RentalComparatorByMovie();

            rentals.sort(cmp);
            int[] nr_rented = new int[Math.toIntExact(rentals.get(rentals.size() - 1).getMovie().getId())];

            for (Rental r : rentals)
            {
                ++nr_rented[Math.toIntExact(r.getMovie().getId()) - 1];
            }

            int max = -1;
            for(int i : nr_rented)
            {
                if(i > max)
                    max = i;
            }

            Set<Movie> most_rented = new HashSet<>();
            for(int i = 0; i < nr_rented.length; ++i)
            {
                if(nr_rented[i] == max)
                {
                    most_rented.add(movie_repo.findOne((long)i + 1).get());
                }
            }

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
