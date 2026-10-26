-- liquibase formatted sql
-- changeset author:farida:5
update speakers
set company = 'Google Cloud'
where email = 'ali@example.com';