create table if not exists post (
    id serial primary key,
	name text,
	text text unique,
	link text,
    created timestamp
);