INSERT INTO users (id, username, password, email)
VALUES
    ('6556437c-2a30-4e10-bfee-7239c5fc9e12', 'teszt', '$2a$10$46v7LgzwgYlUbP8gtoSJD.DfGtVM90ZBtPR4cqfKQdALhbsgeLZ3G', 'teszt@teszt.com');

INSERT INTO lists (id, owner_id, name)
VALUES
    ('ea5768b5-88c4-40a0-a639-0a6819f849e9', '6556437c-2a30-4e10-bfee-7239c5fc9e12', 'Teszt lista');

INSERT INTO list_items (id, list_id, name, quantity, unit, is_checked)
VALUES
    ('2d522d18-8bc3-41b0-9de1-4fe56a2b1616', 'ea5768b5-88c4-40a0-a639-0a6819f849e9', 'Teszt elem', 1, 'kg', false);
