package ru.otus.books.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.books.exceptions.BookRepositoryException;
import ru.otus.books.model.Author;
import ru.otus.books.model.Book;
import ru.otus.books.model.Genre;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({DefaultBookRepository.class})
class BookRepositoryTest {

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
    private BookRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnBookById() {
        assertThat(repository.getById(EXISTING_BOOK_1.getId()))
                .usingRecursiveComparison()
                .isEqualTo(EXISTING_BOOK_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingBookByNonExistingId() {
        assertThatThrownBy(() -> repository.getById(NON_EXISTING_BOOK_ID))
                .isInstanceOf(BookRepositoryException.class)
                .hasMessage("error getting book by id " + NON_EXISTING_BOOK_ID)
                .hasCauseInstanceOf(NoResultException.class);
    }

    @Test
    void shouldReturnAllBooks() {
        assertThat(repository.getAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(EXISTING_BOOK_1, EXISTING_BOOK_2);
    }

    @Test
    void shouldCreateBook() {
        Book book = Book.builder()
                .title("new book")
                .author(EXISTING_AUTHOR_1)
                .genre(EXISTING_GENRE_1)
                .build();

        Book createdBook = repository.create(book);

        assertThat(em.find(Book.class, createdBook.getId()))
                .usingRecursiveComparison()
                .isEqualTo(book);
    }

    @Test
    void shouldThrowExceptionWhileCreatingDuplicateBookTitle() {
        Book book = Book.builder()
                .title(EXISTING_BOOK_1.getTitle())
                .author(EXISTING_AUTHOR_1)
                .genre(EXISTING_GENRE_1)
                .build();
        assertThatThrownBy(() -> repository.create(book))
                .isInstanceOf(BookRepositoryException.class)
                .hasMessage("error during book creating Book(id=null, title=title_1, genre=Genre(id=1, title=title_1), author=Author(id=1, firstName=first_name_1, lastName=last_name_1))")
                .hasCauseInstanceOf(PersistenceException.class);
    }

    @Test
    void shouldUpdateBook() {
        Book expected = Book.builder()
                .id(EXISTING_BOOK_1.getId())
                .title("new title")
                .author(EXISTING_BOOK_1.getAuthor())
                .genre(EXISTING_BOOK_1.getGenre())
                .build();

        assertThat(repository.update(EXISTING_BOOK_1.getId(), "new title"))
                .isEqualTo(expected);
    }

    @Test
    void shouldDeleteBookById() {
        Book existing = em.find(Book.class, EXISTING_BOOK_2.getId());
        assertThat(existing).isNotNull();

        repository.deleteById(EXISTING_BOOK_2.getId());
        em.detach(existing);

        assertThat(em.find(Book.class, EXISTING_BOOK_2.getId())).isNull();
    }
}
