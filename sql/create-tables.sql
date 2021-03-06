-- This file creates all multilog tables.

-- Creates the user type enumeration.
CREATE TYPE e_user_type AS ENUM ('USER', 'MOD', 'ADMIN');

-- Creates the user table.
CREATE TABLE users (
  user_id        BIGSERIAL PRIMARY KEY,
  username       VARCHAR(25) NOT NULL,
  salt	         VARCHAR(32) NOT NULL,
  passwd_hash	 VARCHAR(64) NOT NULL,
  first_name     VARCHAR(50) NOT NULL,
  last_name      VARCHAR(50) NOT NULL,
  email          VARCHAR(50) NOT NULL,
  show_real_name BOOLEAN DEFAULT TRUE,
  show_email     BOOLEAN DEFAULT TRUE,
  description    TEXT,
  user_type      e_user_type DEFAULT 'USER' NOT NULL,
  image          BYTEA,
  ban_until      TIMESTAMP WITH TIME ZONE,
  created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at     TIMESTAMP WITH TIME ZONE NOT NULL,
  removed        BOOLEAN DEFAULT FALSE);

-- Creates the topic table.
CREATE TABLE topics (
  topic_id   BIGSERIAL PRIMARY KEY,
  topic_name VARCHAR(50) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL); 

-- Creates the thread table.
CREATE TABLE threads (
  thread_id   BIGSERIAL PRIMARY KEY,
  topic_id    BIGSERIAL REFERENCES topics(topic_id),
  thread_name VARCHAR(50) NOT NULL,
  created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at  TIMESTAMP WITH TIME ZONE NOT NULL);

-- Create the post table.
CREATE TABLE posts (
  post_id     BIGSERIAL PRIMARY KEY,
  thread_id   BIGSERIAL REFERENCES threads(thread_id),
  user_id     BIGSERIAL REFERENCES users(user_id),
  post_text   TEXT NOT NULL,
  parent_post BIGSERIAL, -- Set if this post is a reply..
  removed     BOOLEAN DEFAULT FALSE,
  created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at  TIMESTAMP WITH TIME ZONE NOT NULL);

-- Create the table containing info about the messages that users have 'read'.
CREATE TABLE message_reads (
  user_id BIGSERIAL NOT NULL,
  post_id BIGSERIAL NOT NULL,
  PRIMARY KEY(user_id, post_id));
