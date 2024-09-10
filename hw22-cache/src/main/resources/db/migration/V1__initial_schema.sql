-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
-- create sequence client_SEQ start with 1 increment by 1;
--
-- create table client
-- (
--     id   bigint not null primary key,
--     name varchar(50)
-- );

create sequence address_seq start with 1 increment by 1;
create sequence client_seq start with 1 increment by 1;
create sequence phone_seq start with 1 increment by 1;
create table address
(
    address_owner_id bigint unique,
    id               bigint not null,
    street           varchar(255),
    primary key (id)
);
create table client
(
    id   bigint not null,
    name varchar(255),
    primary key (id)
);
create table phone
(
    id             bigint not null,
    phone_owner_id bigint,
    number         varchar(255),
    primary key (id)
);
alter table if exists address add constraint fk_address_client foreign key (address_owner_id) references client;
alter table if exists phone add constraint fk_phone_client foreign key (phone_owner_id) references client;