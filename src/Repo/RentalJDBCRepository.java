package Repo;

import Domain.Client;
import Domain.Movie;
import Domain.Rental;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RentalJDBCRepository implements Repository<Long, Rental>
{
    private Connection c;
    private Validator<Rental> validator;

    public RentalJDBCRepository(Validator<Rental> cv)
    {
        this.validator = cv;
        c= null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/movierental",
                    "postgres", "plang");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Rental> findOne(Long id)
    {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");

        String sql = "select * from rentals where rentalid=?";

        try
        {
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setLong(1, id);
            ResultSet rs = pstm.executeQuery();

            if(rs.next())
            {
                Movie m = new Movie("", "", 0);
                Client cl = new Client("", 0);
                m.setId(rs.getLong("movieid"));
                cl.setId(rs.getLong("clientid"));

                Rental r = new Rental(m, cl, rs.getBoolean("returned"));
                r.setId(rs.getLong("rentalid"));
                return Optional.of(r);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Rental> findAll() {
        Set<Rental> all = new HashSet<>();
        String sql = "select * from rentals";

        try
        {
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while(rs.next())
            {
                Movie m = new Movie("", "", 0);
                Client cl = new Client("", 0);
                m.setId(rs.getLong("movieid"));
                cl.setId(rs.getLong("clientid"));

                Rental r = new Rental(m, cl, rs.getBoolean("returned"));
                r.setId(rs.getLong("rentalid"));
                all.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return all;
    }

    @Override
    public Optional<Rental> save(Rental entity) throws ValidatorException
    {
        if(entity == null)
            throw new IllegalArgumentException("entity must not be null");

        validator.validate(entity);
        String sql = "insert into rentals(movieid, clientid, returned) values(?,?,?)";

        try
        {
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setLong(1, entity.getMovie().getId());
            pstm.setLong(2, entity.getClient().getId());
            pstm.setBoolean(3, false);


            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rental> delete(Long id)
    {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");

        String sql = "delete from rentals where retnalid = ?";

        try
        {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Rental> update(Rental entity) throws ValidatorException
    {
        if(entity == null)
            throw new IllegalArgumentException("entit can't be null");
        validator.validate(entity);

        String sql = "update rentals set movieid = ?, clientid = ?, returned = ? where rentalid = ?";

        try
        {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setLong(1, entity.getMovie().getId());
            ps.setLong(2, entity.getClient().getId());
            ps.setBoolean(1, entity.isReturned());
            ps.setLong(4, entity.getId());

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public int size()
    {
        int nr_rentals = 0;
        String sql = "SELECT COUNT(*) AS total FROM rentals";

        try
        {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet res = ps.executeQuery();

            if(res.next())
            {
                nr_rentals = res.getInt("total");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return nr_rentals;
    }
}
