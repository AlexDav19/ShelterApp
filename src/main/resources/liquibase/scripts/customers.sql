-- liquibase formatted sql

-- changeset mikhail-lukichev:1
CREATE TABLE customers
(
    id           bigserial primary key,
    chat_id      bigserial,
    name         varchar,
    phone        varchar
);

