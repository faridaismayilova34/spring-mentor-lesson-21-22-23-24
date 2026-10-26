-- liquibase formatted sql
-- changeset author:farida:7
create table temp_logs (
                           id BIGSERIAL primary key ,
                           message varchar(255)
);

drop table temp_logs;
