-- liquibase formatted sql

-- changeset mikhail-lukichev:1
CREATE TABLE adoptions
(
    id           bigserial primary key,
    adopter_id   bigserial,
    pet_id       bigserial,
    trial_end    TIMESTAMP,
    last_report  TIMESTAMP,
    trial_success boolean
);

