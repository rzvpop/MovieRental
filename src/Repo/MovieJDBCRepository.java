package Repo;


import Domain.Movie;
import Domain.Validators.MovieValidator;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MovieJDBCRepository implements Repository<Long, Movie> {
    private Connection c;
    private Validator<Movie> validator;

    public MovieJDBCRepository(Validator<Movie> mv)
    {
        this.validator = mv;
        c = null;

        try {
            //Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/movierental",
                            "postgres", "plang");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Movie> findOne(Long id)
    {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");

        String sql = "select * from Movies where movieid = ?";
        try
        {
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setLong(1, id);
            ResultSet rs = pstm.executeQuery();

            if(rs.next())
            {
                Movie mv = new Movie(rs.getString("title"),
                        rs.getString("release_date"),
                        rs.getInt("price"));
                mv.setId((long) rs.getInt("movieid"));

                return Optional.of(mv);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }



    @Override
    public Iterable<Movie> findAll() {
        Set<Movie> allEntities = new HashSet<>();
        String sql = "select * from movies";

        try
        {
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while(rs.next())
            {
                Movie mv = new Movie(rs.getString("title"),
                        rs.getString("release_date"),
                        rs.getInt("price"));
                mv.setId((long) rs.getInt("movieid"));
                allEntities.add(mv);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return allEntities;
    }

    @Override
    public Optional<Movie> save(Movie entity) throws ValidatorException
    {
        if(entity == null)
            throw new IllegalArgumentException("entity must not be null");

        validator.validate(entity);
        String sql = "insert into movies(title, release_date, price) values(?, ?, ?)";

        try{
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setString(1, entity.getName());
            pstm.setString(2, entity.getDate_time());
            pstm.setInt(3, entity.getPrice());

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Movie> delete(Long id)
    {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");

        try
        {
            PreparedStatement ps = c.prepareStatement("delete from Movie where movieid=" + id);
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
    public Optional<Movie> update(Movie entity) throws ValidatorException {
        if(entity == null)
            throw new IllegalArgumentException("entity must not be null");

        validator.validate(entity);

        String sql = "update Movies set title = ?, release_date = ?, price = ? where movieid = ?";
        try{
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setString(1, entity.getName());
            pstm.setString(2, entity.getDate_time());
            pstm.setInt(3, entity.getPrice());
            pstm.setLong(4, entity.getId());

            pstm.executeUpdate();
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
        int nr_movies = 0;
        String sql = "SELECT COUNT(*) AS total FROM movies";

        try
        {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet res = ps.executeQuery();

            if(res.next())
            {
                nr_movies = res.getInt("total");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return nr_movies;
    }
}
