package Repo;

import Domain.Client;
import Domain.Movie;
import Domain.Rental;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repo.InMemoryRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RentalXmlRepository extends InMemoryRepository<Long, Rental> {
    private String fileName;

    public RentalXmlRepository(Validator<Rental> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        loadData();
    }

    private void loadData() {
        Path path = Paths.get(fileName);
        try {

            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));
                Long id = Long.valueOf(items.get(0));
                String name = items.get(1);
                int age = Integer.parseInt(items.get((2)));
                Client client = new Client(name, age);
                client.setId(id);

                Long id2 = Long.valueOf(items.get(3));
                String nume = items.get(4);
                String date = items.get((5));
                int price = Integer.parseInt(items.get(6));
                Movie film = new Movie(nume, date, price);
                film.setId(id2);

                boolean rez = Boolean.parseBoolean(items.get(7));

                Rental newrental = new Rental(film, client, rez);

                try {
                    super.save(newrental);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Rental> save(Rental entity) throws ValidatorException {
        Optional<Rental> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    private void saveToFile() {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for(Rental r : super.findAll()) {
                bufferedWriter.write(r.csv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Rental> delete(Long id) {
        Optional<Rental> optional = super.delete(id);
        saveToFile();
        if (optional.isPresent()) {
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rental> update(Rental entity) {
        Optional<Rental> optional = super.update(entity);
        saveToFile();
        if(optional.isPresent()) {
            return optional;
        }
        return Optional.empty();
    }
}
