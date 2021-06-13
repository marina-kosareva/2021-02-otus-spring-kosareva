INSERT INTO authors (id, first_name, last_name)
VALUES (1, 'Robert', 'Martin'),
       (2, 'Guzel', 'Yahina');

INSERT INTO genres (id, title)
VALUES (1, 'novel'),
       (2, 'professional');

INSERT INTO books (id, title, author_id, genre_id)
VALUES (1, 'Clean code', 1, 2),
       (2, 'Zuleikha opens her eyes', 2, 1),
       (3, 'book3_RobinMartin_novel', 1, 1),
       (4, 'book4_GuzelYahina_professional', 2, 2);
