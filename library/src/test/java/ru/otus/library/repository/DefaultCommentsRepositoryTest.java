package ru.otus.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataMongoTest
@Import({DefaultCommentsRepository.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DefaultCommentsRepositoryTest {

    @Autowired
    private CommentsRepository repository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    void findCommentsByBookId() {
        assertThat(repository.findCommentsByBookId("book2Id"))
                .hasSize(2)
                .extracting("text")
                .containsExactlyInAnyOrder("comment1", "comment2");
    }

    @Test
    void createCommentForBook() {
        Book book = mongoOperations.findOne(Query.query(Criteria.where("_id").is("book1Id")), Book.class);
        assertThat(book).isNotNull();
        assertThat(book.getComments()).isNull();

        repository.createCommentForBook(new Comment("comment for book1"), "book1Id");

        Book bookUpdated = mongoOperations.findOne(Query.query(Criteria.where("_id").is("book1Id")), Book.class);
        assertThat(bookUpdated).isNotNull();
        assertThat(bookUpdated.getComments())
                .hasSize(1)
                .extracting("text")
                .containsExactly("comment for book1");
    }

    @Test
    void addCommentForBook() {
        Book book = mongoOperations.findOne(Query.query(Criteria.where("_id").is("book2Id")), Book.class);
        assertThat(book).isNotNull();
        assertThat(book.getComments()).hasSize(2);

        repository.createCommentForBook(new Comment("new comment for book2"), "book2Id");

        Book bookUpdated = mongoOperations.findOne(Query.query(Criteria.where("_id").is("book2Id")), Book.class);
        assertThat(bookUpdated).isNotNull();
        assertThat(bookUpdated.getComments())
                .hasSize(3)
                .extracting("text")
                .containsExactlyInAnyOrder("comment1", "comment2", "new comment for book2");
    }

    @Test
    void deleteCommentByIdForBook() {
        Book book = mongoOperations.findOne(Query.query(Criteria.where("_id").is("book2Id")), Book.class);
        assertThat(book).isNotNull();
        assertThat(book.getComments()).hasSize(2);

        String commentUuid = book.getComments().stream()
                .filter(comment -> "comment1".equals(comment.getText()))
                .map(Comment::getUuid)
                .findFirst()
                .orElse(null);
        assertThat(commentUuid).isNotNull();

        long result = repository.deleteCommentByIdForBook(commentUuid, "book2Id");

        assertThat(result).isOne();
        Book bookUpdated = mongoOperations.findOne(Query.query(Criteria.where("_id").is("book2Id")), Book.class);
        assertThat(bookUpdated).isNotNull();
        assertThat(bookUpdated.getComments())
                .hasSize(1)
                .extracting("text")
                .containsExactlyInAnyOrder("comment2");
    }
}
