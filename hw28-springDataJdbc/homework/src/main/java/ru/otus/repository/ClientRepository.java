package ru.otus.repository;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {
    List<Client> findClientsByNameContainsIgnoreCase(String name);
}
