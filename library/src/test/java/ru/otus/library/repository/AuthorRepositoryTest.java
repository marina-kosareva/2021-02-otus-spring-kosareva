package ru.otus.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.listener.CascadeMongoEventListener;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataMongoTest
@Import({CascadeMongoEventListener.class})
class AuthorRepositoryTest {

    private static final Author AUTHOR = Author.builder()
            .id("author1Id")
            .firstName("firstName1")
            .lastName("lastName1")
            .build();
    private static final Genre GENRE = Genre.builder()
            .id("genre1Id")
            .title("genre1")
            .build();
    private static final Book BOOK = Book.builder()
            .id("book1Id")
            .author(AUTHOR)
            .genre(GENRE)
            .title("book1")
            .build();

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldDeleteBookIfAuthorIsDeleted() {
        assertThat(repository.findById(AUTHOR.getId())).isNotEmpty();
        assertThat(bookRepository.findById("book1Id")).isNotEmpty();

        repository.deleteById(AUTHOR.getId());

        assertThat(repository.findById(AUTHOR.getId())).isEmpty();
        assertThat(bookRepository.findById(BOOK.getId())).isEmpty();

        // return to initial state
        repository.save(AUTHOR);
        bookRepository.save(BOOK);
    }

    @Test
    void shouldUpdateBookIfAuthorIsUpdated() {
        assertThat(repository.findById(AUTHOR.getId())).isNotEmpty().hasValue(AUTHOR);
        assertThat(bookRepository.findById(BOOK.getId())).isNotEmpty().hasValue(BOOK);

        Author authorUpdated = Author.builder()
                .id(AUTHOR.getId())
                .firstName("firstName1")
                .lastName("lastName1")
                .build();
        Book bookUpdated = Book.builder()
                .id(BOOK.getId())
                .author(authorUpdated)
                .genre(BOOK.getGenre())
                .title(BOOK.getTitle())
                .build();
        repository.save(authorUpdated);

        assertThat(repository.findById(AUTHOR.getId())).hasValue(authorUpdated);
        assertThat(bookRepository.findById(BOOK.getId())).hasValue(bookUpdated);

        // return to initial state
        repository.save(AUTHOR);
        assertThat(bookRepository.findById(BOOK.getId())).hasValue(BOOK);
    }

}
