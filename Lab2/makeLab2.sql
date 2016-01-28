-- We disable foreign key checks temporarily so we can delete the
-- tables in arbitrary order, and so insertion is faster.

set FOREIGN_KEY_CHECKS = 0;

-- Drop the tables if they already exist.

drop table if exists users;
drop table if exists theaters;
drop table if exists movies;
drop table if exists performances;
drop table if exists reservations;

-- Create the tables. The 'check' constraints are not effective in MySQL. 
create table users (
  id          integer NOT NULL AUTO_INCREMENT,
  username    varchar(255) UNIQUE,
  tel_nbr     varchar(15),
  address     varchar(255),
  PRIMARY KEY (id)
);

create table theaters (
  id          integer NOT NULL AUTO_INCREMENT,
  name        varchar(255) NOT NULL,
  max_seats   integer NOT NULL,
  PRIMARY KEY (id)
);

create table movies (
  id          integer NOT NULL AUTO_INCREMENT,
  name        varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

create table performances (
  id          integer NOT NULL AUTO_INCREMENT,
  movie_id    integer NOT NULL,
  theater_id  integer NOT NULL,
  performance_date date NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (movie_id) REFERENCES movies(id),
  FOREIGN KEY (theater_id) REFERENCES theaters(id)
);

create table reservations (
  id          integer NOT NULL AUTO_INCREMENT, -- reservation
  user_id     integer NOT NULL,
  performance_id integer NOT NULL,
  PRIMARY KEY (id)
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (performance_id) REFERENCES performances(id)
);

-- We will do a lot of inserts, so we start a transaction to make it faster.

start transaction;

-- add stuff here

-- Commit the transaction.

commit;

-- And re-enable foreign key checks.

set FOREIGN_KEY_CHECKS = 1;

