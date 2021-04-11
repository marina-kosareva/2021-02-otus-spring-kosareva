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
class GenreRepositoryTest {

    private static Author AUTHOR = Author.builder()
            .id("author1Id")
            .firstName("firstName1")
            .lastName("lastName1")
            .build();
    private static Genre GENRE = Genre.builder()
            .id("genre1Id")
            .title("genre1")
            .build();
    private static Book BOOK = Book.builder()
            .id("book1Id")
            .author(AUTHOR)
            .genre(GENRE)
            .title("book1")
            .build();

    @Autowired
    private GenreRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldDeleteBookIfGenreIsDeleted() {
        assertThat(repository.findById(GENRE.getId())).isNotEmpty();
        assertThat(bookRepository.findById("book1Id")).isNotEmpty();

        repository.deleteById(GENRE.getId());

        assertThat(repository.findById(GENRE.getId())).isEmpty();
        assertThat(bookRepository.findById(BOOK.getId())).isEmpty();

        // return to initial state
        repository.save(GENRE);
        bookRepository.save(BOOK);
    }

    @Test
    void shouldUpdateBookIfGenreIsUpdated() {
        assertThat(repository.findById(GENRE.getId())).isNotEmpty().hasValue(GENRE);
        assertThat(bookRepository.findById(BOOK.getId())).isNotEmpty().hasValue(BOOK);

        Genre genreUpdated = Genre.builder()
                .id(GENRE.getId())
                .title("titleUpdated")
                .build();
        Book bookUpdated = Book.builder()
                .id(BOOK.getId())
                .author(BOOK.getAuthor())
                .genre(genreUpdated)
                .title(BOOK.getTitle())
                .build();
        repository.save(genreUpdated);

        assertThat(repository.findById(GENRE.getId())).hasValue(genreUpdated);
        assertThat(bookRepository.findById(BOOK.getId())).hasValue(bookUpdated);

        // return to initial state
        repository.save(GENRE);
        assertThat(bookRepository.findById(BOOK.getId())).hasValue(BOOK);
    }

}
