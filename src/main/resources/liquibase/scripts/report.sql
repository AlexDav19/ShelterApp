-- liquibase formatted sql

-- changeset davletov:1
CREATE TABLE report
(
    id           bigserial primary key,
    adoption_id  bigserial,
    text         varchar,
    photo_id     varchar
);

