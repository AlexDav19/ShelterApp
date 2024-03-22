-- liquibase formatted sql

-- changeset davletov:1
CREATE TABLE volunteers
(
    id           bigserial primary key,
    chat_id      bigserial,
    first_name   varchar,
    user_name    varchar UNIQUE
);

