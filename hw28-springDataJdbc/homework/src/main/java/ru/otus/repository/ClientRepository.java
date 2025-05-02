package ru.otus.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.model.Client;

import java.util.List;

public interface ClientRepository extends ListCrudRepository<Client, Long> {
    List<Client> findClientsByNameContainsIgnoreCase(String name);
}
