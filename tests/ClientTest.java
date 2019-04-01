package nucont_movieRental.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ClientTest {
    private static final String name = "nume";
    private static final String newName = "nnume";
    private static final String email = "mail";
    private static final String newEmail = "nmail";
    private static Set<Long> movies;

    private Client client;

    @Before
    public void setUp() throws Exception {
        client = new Client(name, email);
        client.setId(Long.valueOf(1));
    }

    @After
    public void tearDown() throws Exception {
        client = null;
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("Names should be equal", name, client.getName());
    }

    @Test
    public void testSetName() throws Exception {
        client.setName(newName);
        assertEquals("Names should be equal", newName, client.getName());
    }

    @Test
    public void testGetEmail() throws Exception {
        assertEquals("Emails should be equal", email, client.getEmail());
    }

    @Test
    public void testSetEmail() throws Exception {
        client.setEmail(newEmail);
        assertEquals("Emails should be equal", newEmail, client.getEmail());
    }

    @Test
    public void testGetRentedMovies() throws Exception {
        movies = client.getRentedMovies();
        assertEquals("Client should have no rented movies", movies.size(), 0);
    }

    @Test
    public void testRentMovie() throws Exception {
        client.rentMovie(Long.valueOf(2));
        movies = client.getRentedMovies();
        assertEquals("Client should have one rented movie", movies.size(), 1);
    }

    @Test
    public void testReturnMovie() throws Exception {
        client.rentMovie(Long.valueOf(2));
        client.returnMovie(Long.valueOf(2));
        movies = client.getRentedMovies();
        assertEquals("Client should have no rented movies", movies.size(), 0);
    }
}
