# Users schema

# --- !Ups

CREATE SEQUENCE account_id_seq;
CREATE TABLE account (
    id integer NOT NULL DEFAULT nextval('account_id_seq'),
    uid varchar(36) NOT NULL,
    screenName varchar(50) NOT NULL,
    createdAt varchar(50) NOT NULL DEFAULT current_timestamp
);

CREATE SEQUENCE score_id_seq;
CREATE TABLE score (
    id integer NOT NULL DEFAULT nextval('score_id_seq'),
    uid varchar(36) NOT NULL,
    screenName varchar(50) NOT NULL,
    score integer NOT NULL,
    gameId varchar(50) NOT NULL,
    createdAt varchar(50) NOT NULL DEFAULT current_timestamp
);


# --- !Downs

DROP TABLE user;
DROP SEQUENCE user_id_seq;
DROP TABLE score;
DROP SEQUENCE score_id_seq;
