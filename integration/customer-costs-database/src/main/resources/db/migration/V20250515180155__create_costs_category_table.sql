CREATE TABLE costs_category(
    id SERIAL PRIMARY KEY,
    description VARCHAR NOT NULL
);

INSERT INTO costs_category VALUES
            (1, 'Перекази'),
            (2, 'Інтернет-магазини'),
            (3, 'Платежі до бюджету'),
            (4, 'Таксі'),
            (5, 'Краса'),
            (6, 'Ресторани, кафе, бари'),
            (7, 'Освіта'),
            (8, 'Медичні послуги'),
            (9, 'Супермаркети та продукти'),
            (10, 'Аптеки'),
            (11, 'Інше');