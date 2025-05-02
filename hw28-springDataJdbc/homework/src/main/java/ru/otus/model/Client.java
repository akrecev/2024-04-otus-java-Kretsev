package ru.otus.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
public record Client(
        @Id Long id,
        String name,
        @MappedCollection(idColumn = "address_owner_id") Address address,
        @MappedCollection(idColumn = "phone_owner_id") List<Phone> phones) {}
