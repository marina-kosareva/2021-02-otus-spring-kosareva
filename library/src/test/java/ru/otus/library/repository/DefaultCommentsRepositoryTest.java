package ru.otus.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.model.Comment;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataMongoTest
@Import({DefaultCommentsRepository.class})
class DefaultCommentsRepositoryTest {

    @Autowired
    private CommentsRepository repository;

    @Test
    void findCommentsByBookId() {
        assertThat(repository.findCommentsByBookId("book2Id"))
                .hasSize(2)
                .extracting("text")
                .containsExactlyInAnyOrder("comment1", "comment2");
    }

    @Test
    void createCommentForBook() {
        assertThat(repository.findCommentsByBookId("book1Id")).isEmpty();

        repository.createCommentForBook(new Comment("comment for book1"), "book1Id");

        List<Comment> expected = repository.findCommentsByBookId("book1Id");
        assertThat(expected)
                .hasSize(1)
                .extracting("text")
                .containsExactly("comment for book1");
        // return to initial state
        repository.deleteCommentByIdForBook(expected.get(0).getUuid(), "book1Id");
    }

    @Test
    void addCommentForBook() {
        assertThat(repository.findCommentsByBookId("book2Id")).hasSize(2);

        repository.createCommentForBook(new Comment("new comment for book2"), "book2Id");

        List<Comment> expected = repository.findCommentsByBookId("book2Id");
        assertThat(expected)
                .hasSize(3)
                .extracting("text")
                .containsExactlyInAnyOrder("comment1", "comment2", "new comment for book2");

        String addedCommentId = expected.stream()
                .filter(comment -> "new comment for book2".equals(comment.getText()))
                .map(Comment::getUuid)
                .findFirst()
                .orElse(null);
        assertThat(addedCommentId).isNotEmpty();

        // return to initial state
        repository.deleteCommentByIdForBook(addedCommentId, "book1Id");
    }

    @Test
    void deleteCommentByIdForBook() {
        repository.createCommentForBook(new Comment("comment for book3"), "book3Id");
        assertThat(repository.findCommentsByBookId("book3Id")).hasSize(1);
        Comment comment = repository.findCommentsByBookId("book3Id").get(0);

        long result = repository.deleteCommentByIdForBook(comment.getUuid(), "book3Id");

        assertThat(result).isOne();
        assertThat(repository.findCommentsByBookId("book3Id")).isEmpty();
    }
}
