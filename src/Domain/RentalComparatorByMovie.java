package Domain;

import java.util.Comparator;

public class RentalComparatorByMovie<Movie> implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Rental r1 = (Rental)o1, r2 = (Rental)o2;

        return (int)(r1.getMovie().getId() - r2.getMovie().getId());
    }
}
