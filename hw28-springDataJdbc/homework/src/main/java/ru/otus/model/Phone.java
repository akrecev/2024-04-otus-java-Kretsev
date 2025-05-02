package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
public record Phone(
        @Id
        Long id,
        String number
) {}