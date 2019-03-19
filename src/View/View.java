package View;

import Controller.Controller;
import Domain.Client;
import Domain.Movie;
import Domain.Rental;

import java.util.Optional;
import java.util.Scanner;

public class View
{
    private Controller ctrl;
    private long client_id = 0;
    private long movie_id = 0;
    private long rental_id = 0;

    public View(Controller ctrl_p)
    {
        ctrl = ctrl_p;
    }

    private void printOptions()
    {
        System.out.println("Menu:");
        System.out.println("1.Add movie");
        System.out.println("2.Add client");
        System.out.println("3.Rent movie");
        System.out.println("4.Return movie");
        System.out.println("5.All movies");
        System.out.println("6.Print movies which contain string:");
        System.out.println("7.All clients");
        System.out.println("8.All rentals");
        System.out.println("9.Most rented movies");
        System.out.println("0.Exit");
        System.out.println();
    }

    private int getOption(){
        Scanner scanner = new Scanner(System.in);
        int option;
        try {
            option = scanner.nextInt();
        }
        catch (RuntimeException e){
            System.out.print("Invalid input! Try again!");
            return getOption();
        }
        return option;
    }



    private Movie readMovie()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Name:");
        String name = scanner.nextLine();

        System.out.println("Date:");
        String date = scanner.next();

        System.out.println("Price:");
        int price = scanner.nextInt();

        Movie movie = new Movie(name, date, price);
        movie.setId(++movie_id);

        return movie;
    }

    private Client readClient()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Name: ");
        String name = scanner.nextLine();

        System.out.println("Age: ");
        int age = scanner.nextInt();

        Client client = new Client(name, age);
        client.setId(++client_id);

        return client;
    }

    private Rental readRental()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Client id: ");
        long c_id = scanner.nextLong();

        System.out.println("Movie id: ");
        long m_id = scanner.nextLong();

        Optional<Client> c = ctrl.getClientRepo().findOne(c_id);
        Optional<Movie> m = ctrl.getMovieRepo().findOne(m_id);

        if(c.isPresent() && m.isPresent())
        {
            Rental r = new Rental(m.get(), c.get(),false);
            r.setId(++rental_id);

            return r;
        }
        else
            throw new RuntimeException("Movie or client not found.");
    }

    public void run()
    {
        printOptions();

        int option;
        Scanner scanner = new Scanner(System.in);

        System.out.print(">>");
        option = getOption();
        while(option != 0)
        {
            try
            {
                if (option == 1) {
                    Movie movie = readMovie();
                    ctrl.addMovie(movie);
                } else if (option == 2) {
                    Client cl = readClient();
                    ctrl.addClient(cl);
                } else if (option == 3) {
                    Rental r = readRental();
                    ctrl.addRental(r);
                }
                else if(option == 4)
                {
                    System.out.println("Movie id: ");
                    long movie_id = scanner.nextInt();
                    ctrl.returnMovie(movie_id);
                }
                else if (option == 5) {
                    for (Movie movie : ctrl.getAllMovies()) {
                        System.out.println(movie.toString());
                    }
                } else if (option == 6) {
                    String str = scanner.next();

                    for (Movie movie : ctrl.filterMoviesByName(str)) {
                        System.out.println(movie.toString());
                    }
                }
                else if (option == 7) {
                    for (Client c : ctrl.getAllClients()) {
                        System.out.println(c.toString());
                    }
                } else if (option == 8) {
                    for (Rental r : ctrl.getAllRentals()) {
                        System.out.println(r.toString());
                    }
                } else if (option == 9) {
                    for (Movie m : ctrl.mostRented()) {
                        System.out.println(m.toString());
                    }
                } else {
                    System.out.println("Invalid option.");
                }
            }
            catch (RuntimeException e)
            {
                System.out.println(e.toString());
            }

            System.out.print(">>");
            option = getOption();
        }
        }
    }