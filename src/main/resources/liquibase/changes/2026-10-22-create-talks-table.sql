-- liquibase formatted sql
-- changeset author:farida:2
CREATE TABLE talks
(
    id               BIGSERIAL primary key,
    duration_minutes integer,
    room             varchar(255),
    title            varchar(255),
    speaker_id       BIGINT,
    status           varchar(255),
    scheduled_at     timestamp,
    constraint fk_talks_speaker foreign key (speaker_id) references speakers (id)
);