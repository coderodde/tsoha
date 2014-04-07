-- This SQL file drops all tables in multilog.

-- Drops the tables.
DROP TABLE IF EXISTS message_reads, posts, threads, topics, users;

-- Drops everything else.
DROP TYPE e_user_type;

