-- liquibase formatted sql
-- changeset author:farida:4
insert into talks (duration_minutes, room, title, speaker_id, status, scheduled_at)
values (45, 'Room A', 'Java Spring Boot Overview', 1, 'PLANNED', '2026-11-01 10:00:00');

insert into talks (duration_minutes, room, title, speaker_id, status, scheduled_at)
values (60, 'Room B', 'PostgreSQL & Liquibase Integration', 2, 'PLANNED', '2026-11-02 14:00:00');