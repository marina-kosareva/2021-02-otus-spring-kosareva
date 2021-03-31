package ru.otus.books.repository;

import org.springframework.stereotype.Repository;
import ru.otus.books.exceptions.AuthorRepositoryException;
import ru.otus.books.exceptions.BookRepositoryException;
import ru.otus.books.model.Book;

import javax.persistence.*;
import java.util.List;

@Repository
public class DefaultBookRepository implements BookRepository {

    private static final String ID_FILED = "id";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Book getById(Long id) {
        TypedQuery<Book> query = em.createQuery("select b from Book b "
                + " left join fetch b.genre "
                + " left join fetch b.author "
                + " where b.id = :id", Book.class);
        query.setParameter(ID_FILED, id);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw new BookRepositoryException("error getting book by id " + id, ex);
        }
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("select b from Book b "
                + " left join fetch b.genre "
                + " left join fetch b.author ", Book.class).getResultList();
    }

    @Override
    public Book create(Book book) {
        try {
            em.persist(book);
            return book;
        } catch (PersistenceException ex) {
            throw new BookRepositoryException("error during book creating " + book, ex);
        }
    }

    @Override
    public Book update(Long id, String title) {
        Book existing = getById(id);
        try {
            return em.merge(Book.builder()
                    .id(getById(id).getId())
                    .title(title)
                    .author(existing.getAuthor())
                    .genre(existing.getGenre())
                    .build());
        } catch (PersistenceException ex) {
            throw new AuthorRepositoryException("error during book updating ", ex);
        }

    }

    @Override
    public int deleteById(Long id) {
        try {
            Query query = em.createQuery("delete from Book b where b.id = :id");
            query.setParameter(ID_FILED, id);
            return query.executeUpdate();
        } catch (PersistenceException ex) {
            throw new BookRepositoryException("error during book deleting by id " + id, ex);
        }
    }
}
