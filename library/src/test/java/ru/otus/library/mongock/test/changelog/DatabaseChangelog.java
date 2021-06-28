package ru.otus.library.mongock.test.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.library.model.*;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.repository.UserRepository;

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
        Author author1 = authorRepository.save(Author.builder()
                .id("author1Id")
                .firstName("firstName1")
                .lastName("lastName1")
                .build());
        Author author2 = authorRepository.save(Author.builder()
                .id("author2Id")
                .firstName("firstName2")
                .lastName("lastName2")
                .build());
        Genre genre1 = genreRepository.save(Genre.builder()
                .id("genre1Id")
                .title("genre1")
                .build());
        Genre genre2 = genreRepository.save(Genre.builder()
                .id("genre2Id")
                .title("genre2")
                .build());
        Book book1 = Book.builder()
                .id("book1Id")
                .author(author1)
                .genre(genre1)
                .title("book1")
                .build();
        Book book2 = Book.builder()
                .id("book2Id")
                .author(author2)
                .genre(genre1)
                .title("book2")
                .comments(Set.of(new Comment("comment1"), new Comment("comment2")))
                .build();
        Book book3 = Book.builder()
                .id("book3Id")
                .author(author2)
                .genre(genre2)
                .title("book3")
                .build();
        bookRepository.saveAll(asList(book1, book2, book3));
    }

    @ChangeSet(order = "003", id = "insertUsers", author = "kosmar")
    public void insertUsers(UserRepository repository) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);
        repository.save(LibraryUser.builder()
                .id("60da1f1f20d2d654330f3d8a")
                .name("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(Set.of(Roles.USER, Roles.ADMIN))
                .build());
        repository.save(LibraryUser.builder()
                .name("user")
                .password(passwordEncoder.encode("user"))
                .roles(Set.of(Roles.USER))
                .build());
    }
}
