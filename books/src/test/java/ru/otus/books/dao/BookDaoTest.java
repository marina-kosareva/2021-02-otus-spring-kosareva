package ru.otus.books.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.books.exceptions.BookDaoException;
import ru.otus.books.model.Author;
import ru.otus.books.model.Book;
import ru.otus.books.model.Genre;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({BookDaoJdbc.class, GenreDaoJdbc.class, AuthorDaoJdbc.class})
class BookDaoTest {

    private static final Long NON_EXISTING_BOOK_ID = 100L;
    private static final Long NON_EXISTING_AUTHOR_ID = 100L;
    private static final Long NON_EXISTING_GENRE_ID = 100L;

    @Autowired
    private BookDaoJdbc daoJdbc;
    @Autowired
    private GenreDaoJdbc genreDaoJdbc;
    @Autowired
    private AuthorDaoJdbc authorDaoJdbc;

    private static final Genre EXISTING_GENRE_1 = Genre.builder()
            .id(1L)
            .title("title_1")
            .build();
    private static final Genre EXISTING_GENRE_2 = Genre.builder()
            .id(2L)
            .title("title_2")
            .build();
    private static final Author EXISTING_AUTHOR_1 = Author.builder()
            .id(1L)
            .firstName("first_name_1")
            .lastName("last_name_1")
            .build();
    private static final Author EXISTING_AUTHOR_2 = Author.builder()
            .id(2L)
            .firstName("first_name_2")
            .lastName("last_name_2")
            .build();
    private static final Book EXISTING_BOOK_1 = Book.builder()
            .id(1L)
            .title("title_1")
            .author(EXISTING_AUTHOR_1)
            .genre(EXISTING_GENRE_2)
            .build();
    private static final Book EXISTING_BOOK_2 = Book.builder()
            .id(2L)
            .title("title_2")
            .author(EXISTING_AUTHOR_2)
            .genre(EXISTING_GENRE_1)
            .build();

    @Test
    void shouldReturnBookById() {
        assertThat(daoJdbc.getById(EXISTING_BOOK_1.getId())).usingRecursiveComparison()
                .isEqualTo(EXISTING_BOOK_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingBookByNonExistingId() {
        assertThatThrownBy(() -> daoJdbc.getById(NON_EXISTING_BOOK_ID))
                .isInstanceOf(BookDaoException.class)
                .hasMessage("error getting book by id " + NON_EXISTING_BOOK_ID)
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldReturnAllBooks() {
        assertThat(daoJdbc.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
                EXISTING_BOOK_1, EXISTING_BOOK_2);
    }

    @Test
    void shouldCreateBook() {
        Book book = Book.builder()
                .title("new book")
                .author(EXISTING_AUTHOR_1)
                .genre(EXISTING_GENRE_1)
                .build();
        Long id = daoJdbc.create("new book", EXISTING_GENRE_1.getId(), EXISTING_AUTHOR_1.getId());

        assertThat(daoJdbc.getById(id))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(book);
    }

    @Test
    void shouldThrowExceptionWhileCreatingDuplicateBookTitle() {
        assertThatThrownBy(() -> daoJdbc.create(EXISTING_BOOK_1.getTitle(), EXISTING_GENRE_1.getId(), EXISTING_AUTHOR_1.getId()))
                .isInstanceOf(BookDaoException.class)
                .hasMessage("error during book creating")
                .hasCauseInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldThrowExceptionWhileCreatingBookWithUnknownGenre() {
        assertThatThrownBy(() -> daoJdbc.create(EXISTING_BOOK_1.getTitle(), NON_EXISTING_GENRE_ID,
                EXISTING_AUTHOR_1.getId()))
                .isInstanceOf(BookDaoException.class)
                .hasMessage("error during book creating")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldThrowExceptionWhileCreatingBookWithUnknownAuthor() {
        assertThatThrownBy(() -> daoJdbc.create(EXISTING_BOOK_1.getTitle(), EXISTING_GENRE_1.getId(),
                NON_EXISTING_AUTHOR_ID))
                .isInstanceOf(BookDaoException.class)
                .hasMessage("error during book creating")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldUpdateBook() {
        Book expected = Book.builder()
                .id(EXISTING_BOOK_1.getId())
                .title("new title")
                .author(EXISTING_AUTHOR_1)
                .genre(EXISTING_GENRE_2)
                .build();

        int updatedRows = daoJdbc.update(EXISTING_BOOK_1.getId(), "new title");

        assertThat(updatedRows).isEqualTo(1);
        assertThat(daoJdbc.getById(EXISTING_BOOK_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldDeleteBookById() {
        assertThatCode(() -> daoJdbc.getById(EXISTING_BOOK_2.getId())).doesNotThrowAnyException();

        daoJdbc.deleteById(EXISTING_BOOK_2.getId());

        assertThatThrownBy(() -> daoJdbc.getById(EXISTING_BOOK_2.getId()))
                .isInstanceOf(BookDaoException.class)
                .hasMessage("error getting book by id " + EXISTING_BOOK_2.getId())
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldDeleteBookIfGenreDeleted() {
        assertThatCode(() -> daoJdbc.getById(EXISTING_BOOK_2.getId())).doesNotThrowAnyException();

        genreDaoJdbc.deleteById(EXISTING_BOOK_2.getGenre().getId());

        assertThatThrownBy(() -> daoJdbc.getById(EXISTING_BOOK_2.getId()))
                .isInstanceOf(BookDaoException.class)
                .hasMessage("error getting book by id " + EXISTING_BOOK_2.getId())
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldDeleteBookIfAuthorDeleted() {
        assertThatCode(() -> daoJdbc.getById(EXISTING_BOOK_2.getId())).doesNotThrowAnyException();

        authorDaoJdbc.deleteById(EXISTING_BOOK_2.getAuthor().getId());

        assertThatThrownBy(() -> daoJdbc.getById(EXISTING_BOOK_2.getId()))
                .isInstanceOf(BookDaoException.class)
                .hasMessage("error getting book by id " + EXISTING_BOOK_2.getId())
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }
}
