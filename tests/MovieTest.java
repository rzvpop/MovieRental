package nucont_movieRental.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MovieTest {
    private static final Long id = Long.valueOf(1);
    private static final Long newId = Long.valueOf(2);
    private static final String title = "titlu";
    private static final String newTitle = "ntitlu";
    private static final String genre = "gen";
    private static final String newGenre = "ngen";
    private static final int year = 2010;
    private static final int newYear = 2011;

    private Movie movie;

    @Before
    public void setUp() throws Exception {
        movie = new Movie(title, genre, year);
        movie.setId(id);
    }

    @After
    public void tearDown() throws Exception {
        movie = null;
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals("Titles should be equal", title, movie.getTitle());
    }

    @Test
    public void testSetTitle() throws Exception {
        movie.setTitle(newTitle);
        assertEquals("Titles should be equal", newTitle, movie.getTitle());
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals("Ids should be equal", id, movie.getId());
    }

    @Test
    public void testSetId() throws Exception {
        movie.setId(newId);
        assertEquals("Ids should be equal", newId, movie.getId());
    }

    @Test
    public void testGetGenre() throws Exception {
        assertEquals("Genres should be equal", genre, movie.getGenre());
    }

    @Test
    public void testSetGenre() throws Exception {
        movie.setGenre(newGenre);
        assertEquals("Genres should be equal", newGenre, movie.getGenre());
    }

    @Test
    public void testGetYear() throws Exception {
        assertEquals("Years should be equal", year, movie.getYear());
    }

    @Test
    public void testSetYear() throws Exception {
        movie.setYear(newYear);
        assertEquals("Years should be equal", newYear, movie.getYear());
    }
}
