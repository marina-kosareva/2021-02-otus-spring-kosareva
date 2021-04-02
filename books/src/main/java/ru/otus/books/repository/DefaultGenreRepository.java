package ru.otus.books.repository;

import org.springframework.stereotype.Repository;
import ru.otus.books.exceptions.GenreRepositoryException;
import ru.otus.books.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

import static java.util.Optional.ofNullable;

@Repository
public class DefaultGenreRepository implements GenreRepository {

    private static final String ID_FILED = "id";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Genre getById(Long id) {
        return ofNullable(em.find(Genre.class, id))
                .orElseThrow(() -> new GenreRepositoryException("error getting genre by id " + id));
    }

    @Override
    public List<Genre> getAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public Genre create(Genre genre) {
        try {
            em.persist(genre);
            return genre;
        } catch (PersistenceException ex) {
            throw new GenreRepositoryException("error during genre creating " + genre, ex);
        }
    }

    @Override
    public Genre update(Genre genre) {
        try {
            return em.merge(genre);
        } catch (PersistenceException ex) {
            throw new GenreRepositoryException("error during genre updating ", ex);
        }
    }

    @Override
    public int deleteById(Long id) {
        try {
            Query query = em.createQuery("delete from Genre g where g.id = :id");
            query.setParameter(ID_FILED, id);
            return query.executeUpdate();
        } catch (PersistenceException ex) {
            throw new GenreRepositoryException("error during genre deleting by id " + id, ex);
        }
    }
}
