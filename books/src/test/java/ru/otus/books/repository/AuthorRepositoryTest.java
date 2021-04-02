package ru.otus.books.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.books.exceptions.AuthorRepositoryException;
import ru.otus.books.model.Author;
import ru.otus.books.model.Book;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({DefaultAuthorRepository.class})
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
    private AuthorRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnAuthorById() {
        assertThat(repository.getById(EXISTING_AUTHOR_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(EXISTING_AUTHOR_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingAuthorByNonExistingId() {
        assertThatThrownBy(() -> repository.getById(NON_EXISTING_AUTHOR_ID))
                .isInstanceOf(AuthorRepositoryException.class)
                .hasMessage("error getting author by id " + NON_EXISTING_AUTHOR_ID);
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

        assertThat(em.find(Author.class, createdAuthor.getId()))
                .usingRecursiveComparison()
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
        Author existing = em.find(Author.class, EXISTING_AUTHOR_1.getId());
        existing.setLastName("Ivanov");
        existing.setFirstName("Ivan");

        Author expected = author(EXISTING_AUTHOR_1.getId(), "Ivan", "Ivanov");
        repository.update(existing);

        assertThat(em.find(Author.class, EXISTING_AUTHOR_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldDeleteAuthorById() {
        Author existing = em.find(Author.class, EXISTING_AUTHOR_1.getId());
        assertThat(existing).isNotNull();

        assertThat(repository.deleteById(EXISTING_AUTHOR_1.getId())).isEqualTo(1);

        em.detach(existing);
        assertThat(em.find(Author.class, EXISTING_AUTHOR_1.getId())).isNull();
    }

    @Test
    void shouldDeleteBookIfAuthorDeleted() {
        Book existing = em.find(Book.class, EXISTING_BOOK_ID_FOR_AUTHOR_1);
        assertThat(existing).isNotNull();

        repository.deleteById(EXISTING_AUTHOR_1.getId());

        em.detach(existing);
        assertThat(em.find(Book.class, EXISTING_BOOK_ID_FOR_AUTHOR_1)).isNull();

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
