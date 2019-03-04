package Domain;

public class Rental extends BaseEntity<Long>
{
    Movie movie;
    Client client;
    boolean returned;

    public Rental(Movie movie, Client client, boolean _returned)
    {
        this.movie = movie;
        this.client = client;
        this.returned = _returned;
    }

    public Movie getMovie()
    {
        return movie;
    }

    public Client getClient()
    {
        return client;
    }

    public boolean isReturned()
    {
        return returned;
    }

    public void setMovie(Movie movie)
    {
        this.movie = movie;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public void setReturned(boolean _returned)
    {
        this.returned = _returned;
    }

    public String toString()
    {
        return movie.toString() + " / " + client.toString() + " / due date: " + ((returned)?"yes":"no");
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Rental rental = (Rental) o;

        if (!movie.equals(rental.movie))
            return false;

        return client.equals(rental.client);
    }
}
