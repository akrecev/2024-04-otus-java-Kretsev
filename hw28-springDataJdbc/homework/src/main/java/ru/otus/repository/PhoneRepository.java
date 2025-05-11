package ru.otus.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.model.Phone;

public interface PhoneRepository extends ListCrudRepository<Phone, Long> {}
