package ru.otus.books.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.books.exceptions.BookRepositoryException;
import ru.otus.books.exceptions.GenreRepositoryException;
import ru.otus.books.model.Genre;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({DefaultBookRepository.class, DefaultGenreRepository.class})
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
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository repository;

    @Test
    void shouldReturnGenreById() {
        assertThat(repository.getById(EXISTING_GENRE_1.getId())).usingRecursiveComparison()
                .isEqualTo(EXISTING_GENRE_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingGenreByNonExistingId() {
        assertThatThrownBy(() -> repository.getById(NON_EXISTING_GENRE_ID))
                .isInstanceOf(GenreRepositoryException.class)
                .hasMessage("error getting genre by id " + NON_EXISTING_GENRE_ID)
                .hasCauseInstanceOf(NoResultException.class);
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

        assertThat(repository.getById(createdGenre.getId()))
                .usingRecursiveComparison()
                .ignoringFields("id")
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
        Genre expected = Genre.builder()
                .id(EXISTING_GENRE_1.getId())
                .title("new title")
                .build();
        int updatedRows = repository.update(EXISTING_GENRE_1.getId(), "new title");

        Assertions.assertThat(updatedRows).isEqualTo(1);
        assertThat(repository.getById(EXISTING_GENRE_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldNotUpdateGenre() {
        int updatedRows = repository.update(NON_EXISTING_GENRE_ID, "new title");
        Assertions.assertThat(updatedRows).isZero();
    }

    @Test
    void shouldDeleteGenreById() {
        assertThatCode(() -> repository.getById(EXISTING_GENRE_1.getId())).doesNotThrowAnyException();

        repository.deleteById(EXISTING_GENRE_1.getId());

        assertThatThrownBy(() -> repository.getById(EXISTING_GENRE_1.getId()))
                .isInstanceOf(GenreRepositoryException.class)
                .hasMessage("error getting genre by id " + EXISTING_GENRE_1.getId())
                .hasCauseInstanceOf(NoResultException.class);
    }

    @Test
    void shouldDeleteBookIfGenreDeleted() {
        assertThatCode(() -> bookRepository.getById(EXISTING_BOOK_ID_FOR_GENRE_1)).doesNotThrowAnyException();

        repository.deleteById(EXISTING_GENRE_1.getId());

        assertThatThrownBy(() -> bookRepository.getById(EXISTING_BOOK_ID_FOR_GENRE_1))
                .isInstanceOf(BookRepositoryException.class)
                .hasMessage("error getting book by id " + EXISTING_BOOK_ID_FOR_GENRE_1)
                .hasCauseInstanceOf(NoResultException.class);
    }
}
