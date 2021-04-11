package ru.otus.library.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

@Repository
@RequiredArgsConstructor
public class DefaultCommentsRepository implements CommentsRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Comment> findCommentsByBookId(String bookId) {
        MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").is(bookId));
        ProjectionOperation projectionOperation = Aggregation.project("comments.uuid", "comments.text");
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, unwind("comments"), projectionOperation);
        return mongoTemplate.aggregate(aggregation, Book.class, Comment.class).getMappedResults();
    }

    @Override
    public void createCommentForBook(Comment comment, String bookId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(bookId));
        Update update = new Update();
        update.push("comments", comment);
        mongoTemplate.updateFirst(query, update, Book.class);
    }

    @Override
    public long deleteCommentByIdForBook(String id, String bookId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(bookId));
        Update update = new Update().pull("comments", Query.query(Criteria.where("uuid").is(id)));
        return mongoTemplate.updateFirst(query, update, Book.class).getModifiedCount();
    }
}
