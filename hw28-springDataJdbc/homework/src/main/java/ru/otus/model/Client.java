package ru.otus.model;

import jakarta.annotation.Nonnull;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
public record Client(
        @Id Long id,
        @Column("name") @Nonnull String name,
        @MappedCollection(idColumn = "address_owner_id") Address address,
        @MappedCollection(idColumn = "phone_owner_id", keyColumn = "number") List<Phone> phones) {}
