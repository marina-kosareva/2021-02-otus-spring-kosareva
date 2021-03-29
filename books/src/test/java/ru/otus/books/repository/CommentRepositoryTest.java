package ru.otus.books.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.books.exceptions.CommentRepositoryException;
import ru.otus.books.model.Author;
import ru.otus.books.model.Book;
import ru.otus.books.model.Comment;
import ru.otus.books.model.Genre;

import javax.persistence.NoResultException;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({DefaultCommentRepository.class})
class CommentRepositoryTest {

    private static final Long NON_EXISTING_COMMENT_ID = 100L;

    @Autowired
    private CommentRepository repository;

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

    private static final Comment EXISTING_COMMENT_1 = Comment.builder()
            .id(1L)
            .text("comment1_book1")
            .book(EXISTING_BOOK_1)
            .build();
    private static final Comment EXISTING_COMMENT_2 = Comment.builder()
            .id(2L)
            .text("comment2_book1")
            .book(EXISTING_BOOK_1)
            .build();
    private static final Comment EXISTING_COMMENT_3 = Comment.builder()
            .id(3L)
            .text("comment1_book2")
            .book(EXISTING_BOOK_2)
            .build();

    @Test
    void shouldReturnCommentById() {
        assertThat(repository.getById(EXISTING_COMMENT_1.getId()))
                .isEqualTo(EXISTING_COMMENT_1);
    }

    @Test
    void shouldThrowExceptionWhileGettingCommentByNonExistingId() {
        assertThatThrownBy(() -> repository.getById(NON_EXISTING_COMMENT_ID))
                .isInstanceOf(CommentRepositoryException.class)
                .hasMessage("error getting comment by id " + NON_EXISTING_COMMENT_ID)
                .hasCauseInstanceOf(NoResultException.class);
    }

    @Test
    void shouldReturnAllComments() {
        assertThat(repository.getAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(EXISTING_COMMENT_1, EXISTING_COMMENT_2, EXISTING_COMMENT_3);
    }

    @Test
    void shouldReturnAllCommentsByBookId() {
        assertThat(repository.getByBookId(EXISTING_BOOK_1.getId()))
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(EXISTING_COMMENT_1, EXISTING_COMMENT_2);
    }

    @Test
    void shouldCreateComment() {
        Comment createdComment = repository.create("new comment", EXISTING_BOOK_1.getId());

        Comment expected = Comment.builder()
                .id(createdComment.getId())
                .text("new comment")
                .book(EXISTING_BOOK_1)
                .build();
        assertThat(repository.getById(createdComment.getId()))
                .isEqualTo(expected);
    }

    @Test
    void shouldUpdateComment() {
        Comment expected = Comment.builder()
                .id(EXISTING_COMMENT_1.getId())
                .text("new comment")
                .book(EXISTING_BOOK_1)
                .build();

        int updatedRows = repository.update(EXISTING_COMMENT_1.getId(), "new comment");

        assertThat(updatedRows).isEqualTo(1);
        assertThat(repository.getById(EXISTING_COMMENT_1.getId()))
                .isEqualTo(expected);
    }

    @Test
    void shouldDeleteCommentById() {
        assertThatCode(() -> repository.getById(EXISTING_COMMENT_2.getId())).doesNotThrowAnyException();

        repository.deleteById(EXISTING_COMMENT_2.getId());

        assertThatThrownBy(() -> repository.getById(EXISTING_COMMENT_2.getId()))
                .isInstanceOf(CommentRepositoryException.class)
                .hasMessage("error getting comment by id " + EXISTING_COMMENT_2.getId())
                .hasCauseInstanceOf(NoResultException.class);
    }
}
