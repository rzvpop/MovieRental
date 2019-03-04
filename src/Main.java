import Controller.Controller;
import Domain.Movie;
import Domain.Validators.MovieValidator;
import Domain.Validators.Validator;
import Repo.Repo;
import View.View;

public class Main
{
    public static void main(String[] args)
    {
        Validator<Movie> movie_validator = new MovieValidator();

        Repo<Long, Movie> movie_repo = new Repo<>(movie_validator);
        Controller ctrl = new Controller(movie_repo);
        View view = new View(ctrl);

        view.run();
    }
}
