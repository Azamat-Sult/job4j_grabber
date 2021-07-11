CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

insert into company (id, name) values (1, 'Google');
insert into company (id, name) values (2, 'Microsoft');
insert into company (id, name) values (3, 'Intel');
insert into company (id, name) values (5, 'AMD');

insert into person (id, name, company_id) values (1, 'William', 1);
insert into person (id, name, company_id) values (2, 'Emma', 1);
insert into person (id, name, company_id) values (3, 'Mason', 1);
insert into person (id, name, company_id) values (4, 'Olivia', 2);
insert into person (id, name, company_id) values (5, 'James', 3);
insert into person (id, name, company_id) values (6, 'Sophia', 5);