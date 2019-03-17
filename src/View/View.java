package View;

import Controller.Controller;
import Domain.Client;
import Domain.Movie;
import Domain.Rental;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class View
{
    private Controller ctrl;
    long client_id = 0;
    long movie_id = 0;
    long rental_id = 0;

    public View(Controller ctrl_p)
    {
        ctrl = ctrl_p;
    }

    public void printOptions()
    {
        System.out.println("Menu:");
        System.out.println("1.All movies");
        System.out.println("2.Print those which contain string:");
        System.out.println("3.Add movie");
        System.out.println("4.Add client");
        System.out.println("5.Rent movie");
        System.out.println("6.All clients");
        System.out.println("7.All rentals");
        System.out.println("8.Most rented movies");
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

        Movie movie = new Movie(name, date, price);
        movie.setId(++movie_id);

        return movie;
    }

    public Client readClient()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Name: ");
        String name = scanner.next();

        System.out.println("Age: ");
        int age = scanner.nextInt();

        Client client = new Client(name, age);
        client.setId(++client_id);

        return client;
    }

    public Rental readRental()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Client id: ");
        long c_id = scanner.nextLong();
        //String c_name = scanner.next();

        System.out.println("Movie id: ");
        long m_id = scanner.nextLong();
        //String m_name = scanner.next();

        //Set<Client> clients = ctrl.getAllClients();
        //Set<Movie> movie = ctrl.getAllMovies();

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
            else if (option == 4){
                Client cl = readClient();
                ctrl.addClient(cl);
            }
            else if (option == 5){
                Rental r = readRental();
                ctrl.addRental(r);
            }
            else if (option == 6) {
                for (Client c : ctrl.getAllClients()) {
                    System.out.println(c.toString());
                }
            }
            else if(option == 7)
            {
                for (Rental r : ctrl.getAllRentals()) {
                    System.out.println(r.toString());
                }
            }
            else if (option == 8)
            {
                for (Movie m : ctrl.mostRented()){
                    System.out.println(m.toString());
                }
            }
            else
            {
                System.out.println("Invalid option.");
            }

            System.out.print(">>");
            option = getOption();
        }
        }
    }