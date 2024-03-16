-- liquibase formatted sql

-- changeset mikhail-lukichev:1
CREATE TABLE adoptions
(
    id           bigserial primary key,
    customer_id   bigserial,
    pet_id       bigserial,
    trial_end    TIMESTAMP,
    last_report  TIMESTAMP,
    trial_success boolean
);

-- changeset davletov:1
ALTER TABLE adoptions
ADD CONSTRAINT addiction_customer_id FOREIGN KEY (customer_id)
REFERENCES customers (id)
ON DELETE SET NULL
ON UPDATE Set NULL;

-- changeset davletov:2
ALTER TABLE adoptions
ADD CONSTRAINT addiction_pet_id FOREIGN KEY (pet_id)
REFERENCES pets (id)
ON DELETE SET NULL
ON UPDATE Set NULL;

