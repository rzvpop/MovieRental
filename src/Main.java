import Controller.Controller;
import Domain.Client;
import Domain.Movie;
import Domain.Rental;
import Domain.Validators.ClientValidator;
import Domain.Validators.MovieValidator;
import Domain.Validators.RentalValidator;
import Domain.Validators.Validator;
import Repo.Repo;
import View.View;

public class Main
{
    public static void main(String[] args)
    {
        Validator<Movie> movie_validator = new MovieValidator();
        Validator<Client> client_valdiator = new ClientValidator();
        Validator<Rental> rental_validator = new RentalValidator();

        Repo<Long, Movie> movie_repo = new Repo<>(movie_validator);
        Repo<Long, Client> client_repo = new Repo<>(client_valdiator);
        Repo<Long, Rental> rental_repo = new Repo<>(rental_validator);
        Controller ctrl = new Controller(movie_repo, client_repo, rental_repo);
        View view = new View(ctrl);

        view.run();
    }
}
