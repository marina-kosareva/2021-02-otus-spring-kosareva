package ru.otus.books.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.books.exceptions.AuthorDaoException;
import ru.otus.books.exceptions.BookDaoException;
import ru.otus.books.model.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class})
class AuthorDaoTest {

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
    private static final Long NON_EXISTING_AUTHOR_ID = 100L;
    private static final Long EXISTING_BOOK_ID_FOR_AUTHOR_1 = 1L;

    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    @Autowired
    private AuthorDaoJdbc daoJdbc;

    @Test
    void shouldReturnAuthorById() {
        assertThat(daoJdbc.getById(EXISTING_AUTHOR_1.getId())).usingRecursiveComparison()
                .isEqualTo(EXISTING_AUTHOR_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingAuthorByNonExistingId() {
        assertThatThrownBy(() -> daoJdbc.getById(NON_EXISTING_AUTHOR_ID))
                .isInstanceOf(AuthorDaoException.class)
                .hasMessage("error getting author by id " + NON_EXISTING_AUTHOR_ID)
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldReturnAllAuthors() {
        List<Author> actualAuthors = daoJdbc.getAll();
        assertThat(actualAuthors).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
                EXISTING_AUTHOR_1, EXISTING_AUTHOR_2);
    }

    @Test
    void shouldCreateAuthor() {
        Author author = author("Ivan", "Ivanov");

        Long id = daoJdbc.create(author);

        assertThat(daoJdbc.getById(id))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(author);
    }

    @Test
    void shouldThrowExceptionWhileCreatingDuplicateAuthor() {
        Author author = author(EXISTING_AUTHOR_1.getFirstName(), EXISTING_AUTHOR_1.getLastName());

        assertThatThrownBy(() -> daoJdbc.create(author))
                .isInstanceOf(AuthorDaoException.class)
                .hasMessage("error during author creating")
                .hasCauseInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldUpdateAuthor() {
        Author expected = author(EXISTING_AUTHOR_1.getId(), "Ivan", "Ivanov");
        int updatedRows = daoJdbc.update(EXISTING_AUTHOR_1.getId(), "Ivan", "Ivanov");

        assertThat(updatedRows).isEqualTo(1);
        assertThat(daoJdbc.getById(EXISTING_AUTHOR_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldNotUpdateAuthor() {
        int updatedRows = daoJdbc.update(NON_EXISTING_AUTHOR_ID, "Ivan", "Ivanov");
        assertThat(updatedRows).isZero();
    }

    @Test
    void shouldDeleteAuthorById() {
        assertThatCode(() -> daoJdbc.getById(EXISTING_AUTHOR_1.getId())).doesNotThrowAnyException();

        daoJdbc.deleteById(EXISTING_AUTHOR_1.getId());

        assertThatThrownBy(() -> daoJdbc.getById(EXISTING_AUTHOR_1.getId()))
                .isInstanceOf(AuthorDaoException.class)
                .hasMessage("error getting author by id " + EXISTING_AUTHOR_1.getId())
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldDeleteBookIfAuthorDeleted() {
        assertThatCode(() -> bookDaoJdbc.getById(EXISTING_BOOK_ID_FOR_AUTHOR_1)).doesNotThrowAnyException();

        daoJdbc.deleteById(EXISTING_AUTHOR_1.getId());

        assertThatThrownBy(() -> bookDaoJdbc.getById(EXISTING_BOOK_ID_FOR_AUTHOR_1))
                .isInstanceOf(BookDaoException.class)
                .hasMessage("error getting book by id " + EXISTING_BOOK_ID_FOR_AUTHOR_1)
                .hasCauseInstanceOf(EmptyResultDataAccessException.class);
    }

    private Author author(String firstName, String lastName) {
        return author(null, firstName, lastName);
    }

    private Author author(Long id, String firstName, String lastName) {
        return Author.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
