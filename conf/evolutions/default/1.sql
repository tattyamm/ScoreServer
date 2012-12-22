# Users schema

# --- !Ups

CREATE SEQUENCE user_id_seq;
CREATE TABLE user (
    id integer NOT NULL DEFAULT nextval('user_id_seq'),
    uid varchar(255),
    screenName varchar(255),
    createdAt varchar(255)
);

# --- !Downs

DROP TABLE user;
DROP SEQUENCE user_id_seq;
