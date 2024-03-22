-- liquibase formatted sql

-- changeset mikhail-lukichev:1
CREATE TABLE pets
(
    id           bigserial primary key,
    name         varchar,
    breed        varchar,
    age          integer,
    photo        varchar
);

