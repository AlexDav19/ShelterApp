-- liquibase formatted sql

-- changeset davletov:1
CREATE TABLE shelters(
    id                  bigserial primary key,
    address             varchar,
    working_hours       varchar,
    driving_directions  varchar
    );