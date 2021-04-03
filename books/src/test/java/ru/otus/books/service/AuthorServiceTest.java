package ru.otus.books.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.books.exceptions.AuthorRepositoryException;
import ru.otus.books.model.Author;
import ru.otus.books.model.Book;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({DefaultAuthorService.class})
class AuthorServiceTest {

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
    private AuthorService service;
    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnAuthorById() {
        assertThat(service.getById(EXISTING_AUTHOR_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(EXISTING_AUTHOR_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingAuthorByNonExistingId() {
        assertThatThrownBy(() -> service.getById(NON_EXISTING_AUTHOR_ID))
                .isInstanceOf(AuthorRepositoryException.class)
                .hasMessage("error getting author by id " + NON_EXISTING_AUTHOR_ID);
    }

    @Test
    void shouldReturnAllAuthors() {
        assertThat(service.getAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(EXISTING_AUTHOR_1, EXISTING_AUTHOR_2);
    }

    @Test
    void shouldCreateAuthor() {

        Author createdAuthor = service.create("Ivan", "Ivanov");

        Author expected = Author.builder()
                .id(createdAuthor.getId())
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();

        assertThat(em.find(Author.class, createdAuthor.getId())).isEqualTo(expected);
    }

    @Test
    void shouldThrowExceptionWhileCreatingDuplicateAuthor() {
        assertThatThrownBy(() -> service.create(EXISTING_AUTHOR_1.getFirstName(), EXISTING_AUTHOR_1.getLastName()))
                .isInstanceOf(AuthorRepositoryException.class)
                .hasMessage("error during author creating")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldUpdateAuthor() {
        service.update(EXISTING_AUTHOR_1.getId(), "Ivan", "Ivanov");

        Author expected = Author.builder()
                .id(EXISTING_AUTHOR_1.getId())
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();

        assertThat(em.find(Author.class, EXISTING_AUTHOR_1.getId())).isEqualTo(expected);
    }

    @Test
    void shouldNotUpdateAuthor() {
        assertThatThrownBy(() -> service.update(EXISTING_AUTHOR_1.getId(), EXISTING_AUTHOR_2.getFirstName(), EXISTING_AUTHOR_2.getLastName()))
                .isInstanceOf(AuthorRepositoryException.class)
                .hasMessage("error during author updating")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldDeleteAuthorById() {
        assertThat(em.find(Author.class, EXISTING_AUTHOR_1.getId())).isNotNull();

        service.deleteById(EXISTING_AUTHOR_1.getId());

        assertThat(em.find(Author.class, EXISTING_AUTHOR_1.getId())).isNull();
    }

    @Test
    void shouldDeleteBookIfAuthorDeleted() {
        Book existing = em.find(Book.class, EXISTING_BOOK_ID_FOR_AUTHOR_1);
        assertThat(existing).isNotNull();
        service.deleteById(EXISTING_AUTHOR_1.getId());
        em.detach(existing);
        em.flush();
        assertThat(em.find(Book.class, EXISTING_BOOK_ID_FOR_AUTHOR_1)).isNull();
    }
}
