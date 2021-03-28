package ru.otus.books.repository;

import org.springframework.stereotype.Repository;
import ru.otus.books.exceptions.BookRepositoryException;
import ru.otus.books.model.Author;
import ru.otus.books.model.Book;
import ru.otus.books.model.Genre;

import javax.persistence.*;
import java.util.List;

@Repository
public class DefaultBookRepository implements BookRepository {

    private static final String ID_FILED = "id";
    private static final String TITLE_FILED = "title";

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
    public Book create(String title, Long genreId, Long authorId) {
        Book book = Book.builder()
                .title(title)
                .author(em.find(Author.class, authorId))
                .genre(em.find(Genre.class, genreId))
                .build();
        try {
            em.persist(book);
            return book;
        } catch (PersistenceException ex) {
            throw new BookRepositoryException("error during book creating " + book, ex);
        }
    }

    @Override
    public int update(Long id, String title) {
        try {
            Query query = em.createQuery("update Book b set b.title = :title where b.id = :id");
            query.setParameter(ID_FILED, id);
            query.setParameter(TITLE_FILED, title);
            return query.executeUpdate();
        } catch (PersistenceException ex) {
            throw new BookRepositoryException("error during book updating ", ex);
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
