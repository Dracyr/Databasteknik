-- We disable foreign key checks temporarily so we can delete the
-- tables in arbitrary order, and so insertion is faster.

set FOREIGN_KEY_CHECKS = 0;

-- Drop the tables if they already exist.

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS theaters;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS performances;
DROP TABLE IF EXISTS reservations;

-- Create the tables. The 'check' constraints are not effective in MySQL. 
CREATE TABLE users (
  id          integer NOT NULL UNIQUE AUTO_INCREMENT,
  username    varchar(255) UNIQUE,
  tel_nbr     varchar(15),
  address     varchar(255),
  PRIMARY KEY (id)
);

CREATE TABLE theaters (
  id          integer NOT NULL UNIQUE AUTO_INCREMENT,
  name        varchar(255) NOT NULL UNIQUE,
  max_seats   integer NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE movies (
  id          integer NOT NULL UNIQUE AUTO_INCREMENT,
  name        varchar(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE performances (
  id          integer NOT NULL UNIQUE AUTO_INCREMENT,
  movie_id    integer NOT NULL,
  theater_id  integer NOT NULL,
  performance_date date NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (movie_id) REFERENCES movies(id),
  FOREIGN KEY (theater_id) REFERENCES theaters(id),
  CONSTRAINT unique_movie_date UNIQUE(movie_id, performance_date)
);

CREATE TABLE reservations (
  id          integer NOT NULL UNIQUE AUTO_INCREMENT, 
  user_id     integer NOT NULL,
  performance_id integer NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (performance_id) REFERENCES performances(id)
  -- CHECK ((SELECT (max_seats - reserved_seats) FROM seat_reservations WHERE performance_id = performance_id) > 0)
);

-- Was used to check that reservations did not exceed theater seats, men behövdes inte

DROP VIEW if exists seat_reservations;
CREATE VIEW seat_reservations AS
SELECT count(*) AS reserved_seats, performance_id, max_seats, theaters.name AS theater_name
FROM reservations
INNER JOIN performances ON reservations.performance_id = performances.id
INNER JOIN theaters ON performances.theater_id = theaters.id
GROUP BY performance_id;

-- We will do a lot of inserts, so we start a transaction to make it faster.

start transaction;

-- id, username, tel_nbr, address
insert into users values
(1, 'pvpvpv', '1112233444', 'vägen 1'),
(2, 'ohooho', '1112233445', 'vägen 2');

-- id, name, max_seats
insert into theaters values
(1, 'Filmpalatset', 20),
(2, 'Rullhuset', 5);

-- id, name
insert into movies values
(1, 'Star Wars'),
(2, 'Den nya tarantinofilmen'),
(3, 'Svenskt drama'),
(4, 'Drop the table');

-- id, movie_id, theater_id, performance_date
insert into performances values
(1, 1, 1, '2016-01-01'),
(2, 2, 1, '2016-01-01'),
(3, 3, 1, '2016-01-01'),
(4, 4, 1, '2016-01-01'),
(5, 1, 2, '2016-01-03'),
(6, 2, 2, '2016-01-02'),
(7, 3, 2, '2016-01-02'),
(8, 4, 2, '2016-01-02'),
(9, 1, 2, '2016-01-02');

-- id, user_id, performance_id
insert into reservations values
(1, 1, 1),
(2, 1, 1),
(3, 1, 1),
(4, 1, 1),
(5, 1, 2),
(6, 2, 2),
(7, 2, 7),
(8, 2, 7),
(9, 2, 7);

-- Commit the transaction.

commit;

-- And re-enable foreign key checks.

set FOREIGN_KEY_CHECKS = 1;