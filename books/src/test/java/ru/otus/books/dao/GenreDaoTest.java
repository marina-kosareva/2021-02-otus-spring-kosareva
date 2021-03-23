package ru.otus.books.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.books.exceptions.BookDaoException;
import ru.otus.books.exceptions.GenreDaoException;
import ru.otus.books.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({BookDaoJdbc.class, GenreDaoJdbc.class})
class GenreDaoTest {

    private static final Genre EXISTING_GENRE_1 = Genre.builder()
            .id(1L)
            .title("title_1")
            .build();
    private static final Genre EXISTING_GENRE_2 = Genre.builder()
            .id(2L)
            .title("title_2")
            .build();
    private static final Long NON_EXISTING_GENRE_ID = 100L;
    private static final Long EXISTING_BOOK_ID_FOR_GENRE_1 = 2L;

    @Autowired
    private GenreDaoJdbc daoJdbc;
    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    @Test
    void shouldReturnGenreById() {
        assertThat(daoJdbc.getById(EXISTING_GENRE_1.getId())).usingRecursiveComparison()
                .isEqualTo(EXISTING_GENRE_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingGenreByNonExistingId() {
        assertThatThrownBy(() -> daoJdbc.getById(NON_EXISTING_GENRE_ID))
                .isInstanceOf(GenreDaoException.class)
                .hasMessage("error getting genre by id " + NON_EXISTING_GENRE_ID)
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldReturnAllGenres() {
        List<Genre> actualGenres = daoJdbc.getAll();
        assertThat(actualGenres).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
                EXISTING_GENRE_1, EXISTING_GENRE_2);
    }

    @Test
    void shouldCreateGenre() {
        Genre genre = Genre.builder()
                .title("title")
                .build();

        Long id = daoJdbc.create(genre);

        assertThat(daoJdbc.getById(id))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(genre);
    }

    @Test
    void shouldThrowExceptionWhileCreatingDuplicateGenre() {
        Genre genre = Genre.builder()
                .title(EXISTING_GENRE_1.getTitle())
                .build();

        assertThatThrownBy(() -> daoJdbc.create(genre))
                .isInstanceOf(GenreDaoException.class)
                .hasMessage("error during genre creating")
                .hasCauseInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldUpdateGenre() {
        Genre expected = Genre.builder()
                .id(EXISTING_GENRE_1.getId())
                .title("new title")
                .build();
        int updatedRows = daoJdbc.update(EXISTING_GENRE_1.getId(), "new title");

        assertThat(updatedRows).isEqualTo(1);
        assertThat(daoJdbc.getById(EXISTING_GENRE_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldNotUpdateGenre() {
        int updatedRows = daoJdbc.update(NON_EXISTING_GENRE_ID, "new title");
        assertThat(updatedRows).isZero();
    }

    @Test
    void shouldDeleteGenreById() {
        assertThatCode(() -> daoJdbc.getById(EXISTING_GENRE_1.getId())).doesNotThrowAnyException();

        daoJdbc.deleteById(EXISTING_GENRE_1.getId());

        assertThatThrownBy(() -> daoJdbc.getById(EXISTING_GENRE_1.getId()))
                .isInstanceOf(GenreDaoException.class)
                .hasMessage("error getting genre by id " + EXISTING_GENRE_1.getId())
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldDeleteBookIfGenreDeleted() {
        assertThatCode(() -> bookDaoJdbc.getById(EXISTING_BOOK_ID_FOR_GENRE_1)).doesNotThrowAnyException();

        daoJdbc.deleteById(EXISTING_GENRE_1.getId());

        assertThatThrownBy(() -> bookDaoJdbc.getById(EXISTING_BOOK_ID_FOR_GENRE_1))
                .isInstanceOf(BookDaoException.class)
                .hasMessage("error getting book by id " + EXISTING_BOOK_ID_FOR_GENRE_1)
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }
}
