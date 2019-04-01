package Domain;

import java.util.Arrays;
import java.util.List;

public class Movie extends BaseEntity<Long>
{
    private String name;
    private String date_time;
    private int price;

    public Movie(String info){
        List<String> items = Arrays.asList(info.split(","));
        String name_p = items.get(1);
        String date_p = items.get(2);
        int price_p = Integer.parseInt(items.get(3));
        this.name = name_p;
        this.date_time = date_p;
        this.price = price_p;
    }

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

    public String csv(){
        return getId().toString() + "," + name + "," + date_time + "," + Integer.toString(price);
    }
}
