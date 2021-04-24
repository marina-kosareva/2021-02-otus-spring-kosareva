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
        bookRepository.saveAll(asList(Book.builder()
                        .title("Zuleikha opens her eyes")
                        .author(authorRepository.save(author("Guzel", "Yahina")))
                        .genre(genreRepository.save(genre("Historical fiction")))
                        .comments(new HashSet<>())
                        .build(),
                Book.builder()
                        .title("Clean code")
                        .author(authorRepository.save(author("Robert", "Martin")))
                        .genre(genreRepository.save(genre("Academic")))
                        .comments(Set.of(new Comment("good"), new Comment("very well")))
                        .build()));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "kosmar")
    public void insertGenres(GenreRepository genreRepository) {
        genreRepository.save(genre("Action fiction"));
        genreRepository.save(genre("Adventure fiction"));
        genreRepository.save(genre("Crime fiction"));
        genreRepository.save(genre("Fantasy"));
        genreRepository.save(genre("Horror"));
        genreRepository.save(genre("Romantic fiction"));
        genreRepository.save(genre("Science fiction"));
        genreRepository.save(genre("Biography"));
    }

    private Genre genre(String title) {
        return Genre.builder()
                .title(title)
                .build();
    }

    private Author author(String firstName, String lastName) {
        return Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
