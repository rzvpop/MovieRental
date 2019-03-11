package Domain;

public class Movie extends BaseEntity<Long>
{
    private String name;
    private String date_time;
    private int price;

    public Movie(String name, String date_time, int price)
    {
        this.name = name;
        this.date_time = date_time;
        this.price = price;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Movie movie = (Movie) o;

        if (!name.equals(movie.name))
            return false;

        return date_time.equals(movie.date_time);
    }

	
/**
*
* A simple getter
* @return the name of the file
*/
    public String getName()
    {
        return name;
    }

    public String getDate_time()
    {
        return date_time;
    }

    public int getPrice()
    {
        return price;
    }

    public String toString()
    {
        return "Id: " + getId().toString() + " | " +  name + " | " + date_time + " | price: " + Integer.toString(price);
    }
}
