CREATE TABLE bank(
    id SERIAL PRIMARY KEY,
    description VARCHAR NOT NULL
);

INSERT INTO bank VALUES (1, 'Monobank'),
                        (2, 'Privat24'),
                        (3, 'Oshadbank'),
                        (5, 'Pumbbank');