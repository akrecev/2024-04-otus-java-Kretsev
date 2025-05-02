package ru.otus.service;

import java.util.List;
import ru.otus.model.Client;

public interface ClientService {
    Client save(Client client);

    Client getById(long id);

    List<Client> getByName(String name);

    List<Client> getAll();

    void delete(long id);
}
