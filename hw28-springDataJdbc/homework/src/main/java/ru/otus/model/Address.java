package ru.otus.model;

import jakarta.annotation.Nonnull;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
public record Address(@Column("street") @Nonnull String street, @Column("address_owner_id") Long addressOwnerId) {}
