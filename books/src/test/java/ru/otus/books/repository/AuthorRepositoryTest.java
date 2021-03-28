package ru.otus.books.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.books.exceptions.AuthorRepositoryException;
import ru.otus.books.exceptions.BookRepositoryException;
import ru.otus.books.model.Author;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({DefaultBookRepository.class, DefaultAuthorRepository.class})
class AuthorRepositoryTest {

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
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository repository;

    @Test
    void shouldReturnAuthorById() {
        assertThat(repository.getById(EXISTING_AUTHOR_1.getId())).usingRecursiveComparison()
                .isEqualTo(EXISTING_AUTHOR_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingAuthorByNonExistingId() {
        assertThatThrownBy(() -> repository.getById(NON_EXISTING_AUTHOR_ID))
                .isInstanceOf(AuthorRepositoryException.class)
                .hasMessage("error getting author by id " + NON_EXISTING_AUTHOR_ID)
                .hasCauseInstanceOf(NoResultException.class);
    }

    @Test
    void shouldReturnAllAuthors() {
        List<Author> actualAuthors = repository.getAll();
        assertThat(actualAuthors).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
                EXISTING_AUTHOR_1, EXISTING_AUTHOR_2);
    }

    @Test
    void shouldCreateAuthor() {
        Author author = author("Ivan", "Ivanov");

        Author createdAuthor = repository.create(author);

        assertThat(repository.getById(createdAuthor.getId()))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(author);
    }

    @Test
    void shouldThrowExceptionWhileCreatingDuplicateAuthor() {
        Author author = author(EXISTING_AUTHOR_1.getFirstName(), EXISTING_AUTHOR_1.getLastName());

        assertThatThrownBy(() -> repository.create(author))
                .isInstanceOf(AuthorRepositoryException.class)
                .hasMessage("error during author creating Author(id=null, firstName=first_name_1, lastName=last_name_1)")
                .hasCauseInstanceOf(PersistenceException.class);
    }

    @Test
    void shouldUpdateAuthor() {
        Author expected = author(EXISTING_AUTHOR_1.getId(), "Ivan", "Ivanov");
        int updatedRows = repository.update(EXISTING_AUTHOR_1.getId(), "Ivan", "Ivanov");

        assertThat(updatedRows).isEqualTo(1);
        assertThat(repository.getById(EXISTING_AUTHOR_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldNotUpdateAuthor() {
        int updatedRows = repository.update(NON_EXISTING_AUTHOR_ID, "Ivan", "Ivanov");
        assertThat(updatedRows).isZero();
    }

    @Test
    void shouldDeleteAuthorById() {
        assertThat(repository.getById(EXISTING_AUTHOR_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(EXISTING_AUTHOR_1);

        assertThat(repository.deleteById(EXISTING_AUTHOR_1.getId())).isEqualTo(1);

        assertThatThrownBy(() -> repository.getById(NON_EXISTING_AUTHOR_ID))
                .isInstanceOf(AuthorRepositoryException.class)
                .hasMessage("error getting author by id " + NON_EXISTING_AUTHOR_ID)
                .hasCauseInstanceOf(NoResultException.class);
    }

    @Test
    void shouldDeleteBookIfAuthorDeleted() {
        assertThatCode(() -> bookRepository.getById(EXISTING_BOOK_ID_FOR_AUTHOR_1)).doesNotThrowAnyException();

        repository.deleteById(EXISTING_AUTHOR_1.getId());

        assertThatThrownBy(() -> bookRepository.getById(EXISTING_BOOK_ID_FOR_AUTHOR_1))
                .isInstanceOf(BookRepositoryException.class)
                .hasMessage("error getting book by id " + EXISTING_BOOK_ID_FOR_AUTHOR_1)
                .hasCauseInstanceOf(NoResultException.class);
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
