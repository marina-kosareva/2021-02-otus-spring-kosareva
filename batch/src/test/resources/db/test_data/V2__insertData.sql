INSERT INTO authors (id, first_name, last_name)
VALUES (1, 'first_name_1', 'last_name_1'),
       (2, 'first_name_2', 'last_name_2');

INSERT INTO genres (id, title)
VALUES (1, 'title_1'),
       (2, 'title_2');

INSERT INTO books (id, title, author_id, genre_id)
VALUES (1, 'title_1', 1, 2),
       (2, 'title_2', 2, 1),
       (3, 'title_3', 1, 1),
       (4, 'title_4', 2, 2);
