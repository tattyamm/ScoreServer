# Users schema

# --- !Ups

CREATE SEQUENCE user_id_seq;
CREATE TABLE user (
    id integer NOT NULL DEFAULT nextval('user_id_seq'),
    uid varchar(50) NOT NULL,
    screenName varchar(50) NOT NULL,
    createdAt varchar(23) NOT NULL DEFAULT current_timestamp
);

# --- !Downs

DROP TABLE user;
DROP SEQUENCE user_id_seq;
