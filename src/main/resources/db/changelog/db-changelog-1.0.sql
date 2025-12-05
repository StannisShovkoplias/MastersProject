-- --liquibase formatted sql

--changeset artem:1
create table users
(
    id                bigserial primary key,
    display_name      text    not null,
    username          text    not null unique,
    email             text    not null unique,
    password          text,
    is_email_verified boolean not null,
    bio               text,
    image             text    not null,
    role              text    not null default 'USER'
);
