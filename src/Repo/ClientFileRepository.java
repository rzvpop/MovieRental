package Repo;

import Domain.Client;
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

public class ClientFileRepository extends InMemoryRepository<Long, Client> {
    private String fileName;

    public ClientFileRepository(Validator<Client> validator, String fileName) {
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

                try {
                    super.save(client);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        System.out.println("Intru in save");
        Optional<Client> optional = super.save(entity);
        if (optional.isPresent()) {
           System.out.println("if is present");
          return optional;
        }
        saveToFile();
        System.out.println("GATA IN PULA MEA");
        return Optional.empty();
    }

    private void saveToFile() {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for(Client c : super.findAll()) {
                bufferedWriter.write(c.csv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Client> delete(Long id) {
        Optional<Client> optional = super.delete(id);
        saveToFile();
        if (optional.isPresent()) {
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client entity) {
        Optional<Client> optional = super.update(entity);
        saveToFile();
        if(optional.isPresent()) {
            return optional;
        }
        return Optional.empty();
    }
}
