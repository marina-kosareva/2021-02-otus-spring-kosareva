DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;
DROP TABLE IF EXISTS genres;

CREATE TABLE authors
(
    id         BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    UNIQUE (first_name, last_name)
);

CREATE TABLE genres
(
    id    BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) UNIQUE
);

CREATE TABLE books
(
    id        BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title     VARCHAR(255) UNIQUE,
    author_id BIGINT(20) NOT NULL,
    genre_id  BIGINT(20) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE
);
