package ru.otus.books.repository;

import org.springframework.stereotype.Repository;
import ru.otus.books.exceptions.GenreRepositoryException;
import ru.otus.books.model.Genre;

import javax.persistence.*;
import java.util.List;

@Repository
public class DefaultGenreRepository implements GenreRepository {

    private static final String ID_FILED = "id";
    private static final String TITLE_FILED = "title";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Genre getById(Long id) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.id = :id", Genre.class);
        query.setParameter(ID_FILED, id);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw new GenreRepositoryException("error getting genre by id " + id, ex);
        }
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
    public int update(Long id, String title) {
        try {
            Query query = em.createQuery("update Genre g set g.title = :title where g.id = :id");
            query.setParameter(ID_FILED, id);
            query.setParameter(TITLE_FILED, title);
            return query.executeUpdate();
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
