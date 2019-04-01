package Repo;

import Domain.Movie;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MovieFileRepository extends InMemoryRepository<Long, Movie> {
    private String fileName;

    public MovieFileRepository(Validator<Movie> validator, String fileName) {
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
                String title = items.get(1);
                String genre = items.get((2));
                int year = Integer.parseInt(items.get(3));

                Movie movie = new Movie(title, genre, year);
                movie.setId(id);

                try {
                    super.save(movie);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }}

    @Override
    public Optional<Movie> save (Movie entity) throws ValidatorException {
        Optional<Movie> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    private void saveToFile() {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for(Movie m : super.findAll()) {
                bufferedWriter.write(
                        m.csv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Movie> delete(Long id) {
        Optional<Movie> optional = super.delete(id);
        saveToFile();
        if (optional.isPresent()) {
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public Optional<Movie> update(Movie entity) {
        Optional<Movie> optional = super.update(entity);
        saveToFile();
        if(optional.isPresent()) {
            return optional;
        }
        return Optional.empty();
    }
}
