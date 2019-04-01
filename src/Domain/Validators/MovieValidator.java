package Domain.Validators;

import Domain.Movie;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MovieValidator implements Validator<Movie>
{
    public boolean validDate(String date)
    {
        List<Integer> date_parts = Arrays.stream(date.split("/")).map(Integer::valueOf).
                                    collect(Collectors.toList());

        if(date_parts.size() == 3)
            if(1 <= date_parts.get(0) && date_parts.get(0) <= 31)
                if(1 <= date_parts.get(1) && date_parts.get(1) <= 12)
                    return 1900 <= date_parts.get(2);

        return false;
    }

    @Override
    public void validate(Movie movie) throws ValidatorException
    {
        if(movie.getName().equals(""))
            throw new ValidatorException("Empty string for name not allowed.");

        if(!validDate(movie.getDate_time()))
            throw new ValidatorException("Wrong date format");
    }
}
