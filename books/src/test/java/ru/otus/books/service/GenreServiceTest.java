package ru.otus.books.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.books.exceptions.GenreRepositoryException;
import ru.otus.books.model.Book;
import ru.otus.books.model.Genre;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({DefaultGenreService.class})
class GenreServiceTest {

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
    private GenreService service;
    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnGenreById() {
        assertThat(service.getById(EXISTING_GENRE_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(EXISTING_GENRE_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingGenreByNonExistingId() {
        assertThatThrownBy(() -> service.getById(NON_EXISTING_GENRE_ID))
                .isInstanceOf(GenreRepositoryException.class)
                .hasMessage("error getting genre by id " + NON_EXISTING_GENRE_ID);
    }

    @Test
    void shouldReturnAllGenres() {
        Assertions.assertThat(service.getAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(EXISTING_GENRE_1, EXISTING_GENRE_2);
    }

    @Test
    void shouldCreateGenre() {

        Genre createdGenre = service.create("title");

        Genre expected = Genre.builder()
                .id(createdGenre.getId())
                .title("title")
                .build();

        assertThat(em.find(Genre.class, createdGenre.getId())).isEqualTo(expected);
    }

    @Test
    void shouldThrowExceptionWhileCreatingDuplicateGenre() {

        assertThatThrownBy(() -> service.create(EXISTING_GENRE_1.getTitle()))
                .isInstanceOf(GenreRepositoryException.class)
                .hasMessage("error during genre creating")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldUpdateGenre() {
        service.update(EXISTING_GENRE_1.getId(), "new title");

        Genre expected = Genre.builder()
                .id(EXISTING_GENRE_1.getId())
                .title("new title")
                .build();

        assertThat(em.find(Genre.class, EXISTING_GENRE_1.getId())).isEqualTo(expected);
    }

    @Test
    void shouldNotUpdateGenre() {
        assertThatThrownBy(() -> service.update(EXISTING_GENRE_1.getId(), EXISTING_GENRE_2.getTitle()))
                .isInstanceOf(GenreRepositoryException.class)
                .hasMessage("error during genre updating")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldDeleteGenreById() {
        assertThat(em.find(Genre.class, EXISTING_GENRE_1.getId())).isNotNull();

        service.deleteById(EXISTING_GENRE_1.getId());

        assertThat(em.find(Genre.class, EXISTING_GENRE_1.getId())).isNull();
    }

    @Test
    void shouldDeleteBookIfGenreDeleted() {
        Book existing = em.find(Book.class, EXISTING_BOOK_ID_FOR_GENRE_1);
        assertThat(existing).isNotNull();
        service.deleteById(EXISTING_GENRE_1.getId());
        em.detach(existing);
        em.flush();
        assertThat(em.find(Book.class, EXISTING_BOOK_ID_FOR_GENRE_1)).isNull();

    }
}
