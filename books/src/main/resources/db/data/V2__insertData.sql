INSERT INTO authors (id, first_name, last_name)
VALUES (1, 'Robert', 'Martin'),
       (2, 'Guzel', 'Yahina');

INSERT INTO genres (id, title)
VALUES (1, 'novel'),
       (2, 'professional');

INSERT INTO books (id, title, author_id, genre_id)
VALUES (1, 'Clean code', 1, 2),
       (2, 'Zuleikha opens her eyes', 2, 1);

INSERT INTO comments (text, book_id)
VALUES ('Good book', 2),
       ('Another comment', 2);