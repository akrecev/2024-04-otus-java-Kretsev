package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("client")
public record Client(
        @Id
        Long id,
        String name,
        @MappedCollection(idColumn = "address_owner_id")
        Address address,
        @MappedCollection(idColumn = "phone_owner_id")
        List<Phone> phones
) {}