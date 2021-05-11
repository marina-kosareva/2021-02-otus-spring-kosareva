package ru.otus.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.library.listener.AuthorMongoEventListener;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataMongoTest
@Import({AuthorMongoEventListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorRepositoryTest {

    private static final Author AUTHOR = Author.builder()
            .id("author1Id")
            .firstName("firstName1")
            .lastName("lastName1")
            .build();
    private static final Genre GENRE = Genre.builder()
            .id("genre1Id")
            .title("genre1")
            .build();
    private static final Book BOOK = Book.builder()
            .id("book1Id")
            .version(0L)
            .author(AUTHOR)
            .genre(GENRE)
            .title("book1")
            .build();

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    void shouldDeleteBookIfAuthorIsDeleted() {
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(AUTHOR.getId())), Author.class))
                .isNotNull();
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is("book1Id")), Book.class))
                .isNotNull();

        repository.deleteById(AUTHOR.getId());

        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(AUTHOR.getId())), Author.class))
                .isNull();
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is("book1Id")), Book.class))
                .isNull();
    }

    @Test
    void shouldUpdateBookIfAuthorIsUpdated() {
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(AUTHOR.getId())), Author.class))
                .isEqualTo(AUTHOR);
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(BOOK.getId())), Book.class))
                .isEqualTo(BOOK);

        Author authorUpdated = Author.builder()
                .id(AUTHOR.getId())
                .firstName("firstName1")
                .lastName("lastName1")
                .build();
        Book bookUpdated = Book.builder()
                .version(BOOK.getVersion() + 1)
                .id(BOOK.getId())
                .author(authorUpdated)
                .genre(BOOK.getGenre())
                .title(BOOK.getTitle())
                .build();
        repository.save(authorUpdated);

        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(AUTHOR.getId())), Author.class))
                .isEqualTo(authorUpdated);
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(BOOK.getId())), Book.class))
                .isEqualTo(bookUpdated);
    }
}
