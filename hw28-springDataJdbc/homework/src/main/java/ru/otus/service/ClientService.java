package ru.otus.service;

import java.util.List;
import ru.otus.model.Client;

public interface ClientService {
    Client save(Client client);

    Client getById(long id);

    List<Client> getByNameContains(String name);

    List<Client> getAll();

    void delete(long id);
}
