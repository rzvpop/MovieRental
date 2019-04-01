package Repo;

import Domain.Client;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClientJDBCRepository implements Repository<Long, Client> {
    private Connection c;
    private Validator<Client> validator;

    public ClientJDBCRepository(Validator<Client> cv){
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
    public Optional<Client> findOne(Long id) {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");

        String sql = "select * from clients where clientid=?";

        try
        {
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setInt(1, id.intValue());
            ResultSet rs = pstm.executeQuery();

            if(rs.next())
            {
                Client cl = new Client(rs.getString("name"), rs.getInt("age"));
                cl.setId(rs.getLong("clientid"));
                return Optional.of(cl);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll()
    {
        Set<Client> all = new HashSet<>();
        String sql = "select * from clients";
        try
        {
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while(rs.next()) {
                Client cl = new Client(rs.getString("name"), rs.getInt("age"));
                cl.setId(rs.getLong("clientid"));
                all.add(cl);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return all;
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException
    {
        if(entity == null)
            throw new IllegalArgumentException("entity must not be null");

        validator.validate(entity);
        String sql = "insert into clients(name, age) values(?,?)";

        try
        {
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setString(1, entity.getName());
            pstm.setInt(2, entity.getAge());

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> delete(Long id)
    {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");

        String sql = "delete from clients where client = ?";
        try {
            //c.createStatement().executeUpdate("delete from clients where clientid=" + id);
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setLong(1, id);

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException {
        if(entity == null)
            throw new IllegalArgumentException("entity must not be null");
        validator.validate(entity);

        String sql = "update clients set name = ?, email = ? where clientid = ?";
        try{
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setString(1, entity.getName());
            pstm.setInt(2, entity.getAge());
            pstm.setLong(3, entity.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int size()
    {
        int nr_clients = 0;
        String sql = "SELECT COUNT(*) AS total FROM clients";

        try
        {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet res = ps.executeQuery();

            if(res.next())
            {
                nr_clients = res.getInt("total");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return nr_clients;
    }
}