package View;

import Controller.Controller;
import Domain.Movie;

import java.util.Scanner;

public class View
{
    private Controller ctrl;

    public View(Controller _ctrl)
    {
        ctrl = _ctrl;
    }

    public void printOptions()
    {
        System.out.println("Menu:");
        System.out.println("1.Print all");
        System.out.println("2.Print those which contain string:");
        System.out.println("3.Add movie");
        System.out.println("0.Exit");
        System.out.println();
    }

    public int getOption(){
	Scanner scanner = new Scanner(System.in);
	System.out.print("Enter option: ");
	int option = 0;
	try {
		option = scanner.nextInt();
	}	
	catch (RuntimeException e){
		System.out.print("Invalid input! Try again!");
		return getOption();
	}
	return option;
   }

    public Movie readMovie()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Name:");
        String name = scanner.next();

        System.out.println("Date:");
        String date = scanner.next();

        System.out.println("Price:");
        int price = scanner.nextInt();

        return new Movie(name, date, price);
    }

    public void run()
    {
        printOptions();

        int option = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.print(">>");
	option = getOption();
	while(option != 0)
        {
                if (option == 1)
                {
                    for (Movie movie : ctrl.getAllMovies()) {
                        System.out.println(movie.toString());
                    }
                }
                else if (option == 2)
                {
                    String str = scanner.next();

                    for (Movie movie : ctrl.filterMoviesByName(str)) {
                        System.out.println(movie.toString());
                    }
                }
                else if (option == 3)
                {
                    Movie movie = readMovie();
                    ctrl.addMovie(movie);
                }
                else {
                    System.out.println("Invalid option.");
		}
                System.out.print(">>");
                option = getOption();
        }
    }
}
