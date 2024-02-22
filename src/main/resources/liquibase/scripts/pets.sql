-- liquibase formatted sql

-- changeset mikhail-lukichev:1
CREATE TABLE pets
(
    id           bigserial primary key,
    name         varchar,
    breed        varchar,
    age          integer,
    photo_data   bytea,
    photo_type   varchar,
    photo_size   bigserial
);

