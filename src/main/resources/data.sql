INSERT INTO users (id, username, password, provider, email)
VALUES
    ('6556437c-2a30-4e10-bfee-7239c5fc9e12', 'teszt', '$2a$10$46v7LgzwgYlUbP8gtoSJD.DfGtVM90ZBtPR4cqfKQdALhbsgeLZ3G', 'LOCAL', 'teszt@teszt.com'),
    ('d25adc80-646c-46e9-920e-f086dfd55954', 'teszt2', '$2a$10$46v7LgzwgYlUbP8gtoSJD.DfGtVM90ZBtPR4cqfKQdALhbsgeLZ3G', 'LOCAL', 'teszt2@teszt.com');

INSERT INTO lists (id, name)
VALUES
    ('ea5768b5-88c4-40a0-a639-0a6819f849e9', 'Teszt lista'),
    ('a8c69d27-d189-4eca-889a-2157933d7fd6','Teszt lista 2');

INSERT INTO list_items (id, list_id, name, quantity, unit, is_checked)
VALUES
    ('2d522d18-8bc3-41b0-9de1-4fe56a2b1616', 'ea5768b5-88c4-40a0-a639-0a6819f849e9', 'Teszt elem', 1, 'kg', false);

INSERT INTO user_list_memberships (id, list_id, user_id, role)
VALUES
    ('bde0ab58-887a-414a-8ec7-5f4eacbb206d', 'ea5768b5-88c4-40a0-a639-0a6819f849e9', '6556437c-2a30-4e10-bfee-7239c5fc9e12', 'OWNER'),
    ('8420356e-4804-4a26-b5fb-88b42d08e75e', 'a8c69d27-d189-4eca-889a-2157933d7fd6', '6556437c-2a30-4e10-bfee-7239c5fc9e12', 'MEMBER'),
    ('8420356e-4804-4a26-b5fb-88b42d08e75f', 'a8c69d27-d189-4eca-889a-2157933d7fd6', 'd25adc80-646c-46e9-920e-f086dfd55954', 'MEMBER');
