package ru.otus.books.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.books.exceptions.GenreRepositoryException;
import ru.otus.books.model.Book;
import ru.otus.books.model.Genre;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({DefaultGenreRepository.class})
class GenreRepositoryTest {

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
    private GenreRepository repository;
    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnGenreById() {
        assertThat(repository.getById(EXISTING_GENRE_1.getId())).usingRecursiveComparison()
                .isEqualTo(EXISTING_GENRE_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingGenreByNonExistingId() {
        assertThatThrownBy(() -> repository.getById(NON_EXISTING_GENRE_ID))
                .isInstanceOf(GenreRepositoryException.class)
                .hasMessage("error getting genre by id " + NON_EXISTING_GENRE_ID);
    }

    @Test
    void shouldReturnAllGenres() {
        List<Genre> actualGenres = repository.getAll();
        Assertions.assertThat(actualGenres).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
                EXISTING_GENRE_1, EXISTING_GENRE_2);
    }

    @Test
    void shouldCreateGenre() {
        Genre genre = Genre.builder()
                .title("title")
                .build();

        Genre createdGenre = repository.create(genre);

        assertThat(em.find(Genre.class, createdGenre.getId()))
                .usingRecursiveComparison()
                .isEqualTo(genre);
    }

    @Test
    void shouldThrowExceptionWhileCreatingDuplicateGenre() {
        Genre genre = Genre.builder()
                .title(EXISTING_GENRE_1.getTitle())
                .build();

        assertThatThrownBy(() -> repository.create(genre))
                .isInstanceOf(GenreRepositoryException.class)
                .hasMessage("error during genre creating Genre(id=null, title=title_1)")
                .hasCauseInstanceOf(PersistenceException.class);
    }

    @Test
    void shouldUpdateGenre() {
        Genre existing = em.find(Genre.class, EXISTING_GENRE_1.getId());
        existing.setTitle("new title");

        Genre expected = Genre.builder()
                .id(EXISTING_GENRE_1.getId())
                .title("new title")
                .build();
        repository.update(existing);
        assertThat(em.find(Genre.class, EXISTING_GENRE_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldDeleteGenreById() {
        Genre existing = em.find(Genre.class, EXISTING_GENRE_1.getId());
        assertThat(existing).isNotNull();

        assertThat(repository.deleteById(EXISTING_GENRE_1.getId())).isEqualTo(1);
        em.detach(existing);

        assertThat(em.find(Genre.class, EXISTING_GENRE_1.getId())).isNull();
    }

    @Test
    void shouldDeleteBookIfGenreDeleted() {
        Book existing = em.find(Book.class, EXISTING_BOOK_ID_FOR_GENRE_1);

        assertThat(existing).isNotNull();

        repository.deleteById(EXISTING_GENRE_1.getId());
        em.detach(existing);

        assertThat(em.find(Book.class, EXISTING_BOOK_ID_FOR_GENRE_1)).isNull();
    }
}
