-- liquibase formatted sql
-- changeset author:farida:1
create table speakers
(
    id        BIGSERIAL primary key,
    company   varchar(255),
    email     varchar(255) unique,
    full_name varchar(255)
);