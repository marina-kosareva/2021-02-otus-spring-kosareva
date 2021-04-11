package ru.otus.library.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "kosmar", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertData", author = "kosmar")
    public void insertData(AuthorRepository authorRepository,
                           GenreRepository genreRepository,
                           BookRepository bookRepository) {
        Author authorGuzel = authorRepository.save(Author.builder()
                .firstName("Guzel")
                .lastName("Yahina")
                .build());
        Author authorRobert = authorRepository.save(Author.builder()
                .firstName("Robert")
                .lastName("Martin")
                .build());
        Genre novel = genreRepository.save(Genre.builder()
                .title("novel")
                .build());
        Genre professional = genreRepository.save(Genre.builder()
                .title("professional")
                .build());
        Book book1 = Book.builder()
                .author(authorGuzel)
                .genre(novel)
                .title("Zuleikha opens her eyes")
                .comments(new HashSet<>())
                .build();
        bookRepository.saveAll(asList(book1,
                Book.builder()
                        .author(authorRobert)
                        .genre(professional)
                        .title("Clean code")
                        .comments(Set.of(new Comment("good"), new Comment("very well")))
                        .build()));
    }
}
