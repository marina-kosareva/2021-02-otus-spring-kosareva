package ru.otus.books.repository;

import org.springframework.stereotype.Repository;
import ru.otus.books.exceptions.CommentRepositoryException;
import ru.otus.books.model.Comment;

import javax.persistence.*;
import java.util.List;

@Repository
public class DefaultCommentRepository implements CommentRepository {

    private static final String ID_FILED = "id";
    private static final String TEXT_FILED = "text";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Comment getById(Long id) {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c "
                + " left join fetch c.book "
                + " where c.id = :id", Comment.class);
        query.setParameter(ID_FILED, id);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw new CommentRepositoryException("error getting comment by id " + id, ex);
        }
    }

    @Override
    public List<Comment> getAll() {
        return em.createQuery("select c from Comment c "
                + " left join fetch c.book", Comment.class).getResultList();
    }

    @Override
    public List<Comment> getByBookId(Long bookId) {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c "
                + " left join fetch c.book b "
                + " where b.id = :id", Comment.class);
        query.setParameter(ID_FILED, bookId);
        return query.getResultList();
    }

    @Override
    public Comment create(Comment comment) {
        try {
            em.persist(comment);
            return comment;
        } catch (PersistenceException ex) {
            throw new CommentRepositoryException("error during comment creating " + comment, ex);
        }
    }

    @Override
    public Comment update(Long id, String text) {
        Comment existing = getById(id);
        try {
            return em.merge(Comment.builder()
                    .id(getById(id).getId())
                    .text(text)
                    .book(existing.getBook())
                    .build());
        } catch (PersistenceException ex) {
            throw new CommentRepositoryException("error during comment updating ", ex);
        }
    }

    @Override
    public int deleteById(Long id) {
        try {
            Query query = em.createQuery("delete from Comment c where c.id = :id");
            query.setParameter(ID_FILED, id);
            return query.executeUpdate();
        } catch (PersistenceException ex) {
            throw new CommentRepositoryException("error during comment deleting by id " + id, ex);
        }
    }
}
