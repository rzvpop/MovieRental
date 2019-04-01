import Controller.Controller;
import Domain.Client;
import Domain.Movie;
import Domain.Rental;
import Domain.Validators.ClientValidator;
import Domain.Validators.MovieValidator;
import Domain.Validators.RentalValidator;
import Domain.Validators.Validator;
import Repo.ClientJDBCRepository;
import Repo.MovieJDBCRepository;
import Repo.RentalJDBCRepository;
import Repo.Repository;
import View.View;


public class Main
{
    public static void main(String[] args)
    {
        Validator<Movie> movie_validator = new MovieValidator();
        Validator<Client> client_validator = new ClientValidator();
        Validator<Rental> rental_validator = new RentalValidator();

//        MovieFileRepository movie_repo = new MovieFileRepository(movie_validator, "movie_repo.txt");
//        ClientFileRepository client_repo = new ClientFileRepository(client_validator, "clients_repo.txt");
//        RentalFileRepository rental_repo = new RentalFileRepository(rental_validator, "rental_repo.txt");
//        Repository<Long, Movie> movie_repo = new MovieXmlRepository(movie_validator, "moviesDB.xml");
//        Repository<Long, Client> client_repo = new ClientXmlRepository(client_validator, "clientsDB.xml");
//        Repository<Long, Rental> rental_repo = new RentalXmlRepository(rental_validator, "rentalsDB.xml");
        Repository<Long, Movie> movie_repo = new MovieJDBCRepository(movie_validator);
        Repository<Long, Client> client_repo = new ClientJDBCRepository(client_validator);
        Repository<Long, Rental> rental_repo = new RentalJDBCRepository(rental_validator);

        Controller ctrl = new Controller(movie_repo, client_repo, rental_repo);
        View view = new View(ctrl);

        view.run();
    }
}
