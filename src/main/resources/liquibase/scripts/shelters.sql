-- liquibase formatted sql

-- changeset davletov:1
CREATE TABLE shelters(
    id                  bigserial primary key,
    address             varchar,
    working_hours       varchar,
    driving_directions  varchar
    );

-- changeset mikhail-lukichev:1
ALTER TABLE shelters
ADD COLUMN phone_main varchar,
ADD COLUMN phone_security varchar;