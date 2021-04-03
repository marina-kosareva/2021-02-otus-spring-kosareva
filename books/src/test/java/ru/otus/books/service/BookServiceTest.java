package ru.otus.books.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.books.exceptions.BookRepositoryException;
import ru.otus.books.model.Author;
import ru.otus.books.model.Book;
import ru.otus.books.model.Genre;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({DefaultBookService.class, DefaultAuthorService.class, DefaultGenreService.class})
class BookServiceTest {

    private static final Long NON_EXISTING_BOOK_ID = 100L;

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
    @Autowired
    private BookService service;
    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnBookById() {
        assertThat(service.getById(EXISTING_BOOK_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(EXISTING_BOOK_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingBookByNonExistingId() {
        assertThatThrownBy(() -> service.getById(NON_EXISTING_BOOK_ID))
                .isInstanceOf(BookRepositoryException.class)
                .hasMessage("error getting book by id " + NON_EXISTING_BOOK_ID);
    }

    @Test
    void shouldReturnAllBooks() {
        assertThat(service.getAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(EXISTING_BOOK_1, EXISTING_BOOK_2);
    }

    @Test
    void shouldCreateBook() {
        Book createdBook = service.create("new book", EXISTING_GENRE_1.getId(), EXISTING_AUTHOR_1.getId());

        Book expected = Book.builder()
                .id(createdBook.getId())
                .title("new book")
                .author(EXISTING_AUTHOR_1)
                .genre(EXISTING_GENRE_1)
                .build();
        assertThat(em.find(Book.class, createdBook.getId()))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldThrowExceptionWhileCreatingDuplicateBookTitle() {
        assertThatThrownBy(() -> service.create(EXISTING_BOOK_2.getTitle(), EXISTING_GENRE_1.getId(), EXISTING_AUTHOR_1.getId()))
                .isInstanceOf(BookRepositoryException.class)
                .hasMessage("error during book creating")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldUpdateBook() {
        service.update(EXISTING_BOOK_1.getId(), "new title");

        Book expected = Book.builder()
                .id(EXISTING_BOOK_1.getId())
                .title("new title")
                .author(EXISTING_BOOK_1.getAuthor())
                .genre(EXISTING_BOOK_1.getGenre())
                .build();

        assertThat(em.find(Book.class, EXISTING_BOOK_1.getId())).isEqualTo(expected);
    }

    @Test
    void shouldDeleteBookById() {
        assertThat(em.find(Book.class, EXISTING_BOOK_2.getId())).isNotNull();

        service.deleteById(EXISTING_BOOK_2.getId());

        assertThat(em.find(Book.class, EXISTING_BOOK_2.getId())).isNull();
    }
}
