package ru.otus.books.repository;

import org.springframework.stereotype.Repository;
import ru.otus.books.exceptions.AuthorRepositoryException;
import ru.otus.books.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

import static java.util.Optional.ofNullable;

@Repository
public class DefaultAuthorRepository implements AuthorRepository {

    private static final String ID_FILED = "id";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Author getById(Long id) {
        return ofNullable(em.find(Author.class, id))
                .orElseThrow(() -> new AuthorRepositoryException("error getting author by id " + id));
    }

    @Override
    public List<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public Author create(Author author) {
        try {
            em.persist(author);
            return author;
        } catch (PersistenceException ex) {
            throw new AuthorRepositoryException("error during author creating " + author, ex);
        }
    }

    @Override
    public Author update(Long id, String firstName, String lastName) {
        try {
            return em.merge(Author.builder()
                    .id(getById(id).getId())
                    .firstName(firstName)
                    .lastName(lastName)
                    .build());
        } catch (PersistenceException ex) {
            throw new AuthorRepositoryException("error during author updating ", ex);
        }
    }

    @Override
    public int deleteById(Long id) {
        try {
            Query query = em.createQuery("delete from Author a where a.id = :id");
            query.setParameter(ID_FILED, id);
            return query.executeUpdate();
        } catch (PersistenceException ex) {
            throw new AuthorRepositoryException("error during author deleting by id " + id, ex);
        }
    }
}
