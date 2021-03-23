package ru.otus.books.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.books.exceptions.BookDaoException;
import ru.otus.books.model.Author;
import ru.otus.books.model.Book;
import ru.otus.books.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BookDaoJdbc implements BookDao {

    private static final String TITLE_FILED = "title";
    private static final String ID_FILED = "id";

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Book getById(Long id) {
        try {
            return jdbcOperations.queryForObject("SELECT b.id AS id , b.title AS title, "
                            + "a.id AS author_id, a.first_name AS author_first_name, a.last_name AS author_last_name, "
                            + "g.id AS genre_id, g.title AS genre_title "
                            + "FROM books b "
                            + "LEFT JOIN authors a ON b.author_id = a.id "
                            + "LEFT JOIN genres g ON b.genre_id = g.id  WHERE b.id = :id",
                    Map.of(ID_FILED, id), new BooksMapper());
        } catch (DataAccessException ex) {
            throw new BookDaoException("error getting book by id " + id, ex);
        }
    }

    @Override
    public List<Book> getAll() {
        return jdbcOperations.query("SELECT b.id AS id , b.title AS title, "
                + "a.id AS author_id, a.first_name AS author_first_name, a.last_name AS author_last_name, "
                + "g.id AS genre_id, g.title AS genre_title "
                + "FROM books b "
                + "LEFT JOIN authors a ON b.author_id = a.id "
                + "LEFT JOIN genres g ON b.genre_id = g.id ", new BooksMapper());
    }

    @Override
    public Long create(String title, Long genreId, Long authorId) {
        SqlParameterSource params = new MapSqlParameterSource(Map.of(
                TITLE_FILED, title,
                "genreId", genreId,
                "authorId", authorId));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcOperations.update("insert into books (`title`,`genre_id`,`author_id` ) values (:title, :genreId, :authorId)",
                    params, keyHolder);
            return keyHolder.getKeyAs(Long.class);
        } catch (DataAccessException ex) {
            throw new BookDaoException("error during book creating", ex);
        }
    }

    @Override
    public int update(Long id, String title) {
        Map<String, Object> params = Map.of(ID_FILED, id, TITLE_FILED, title);
        try {
            return jdbcOperations.update("update books set title = :title where id = :id",
                    params);
        } catch (DataAccessException ex) {
            throw new BookDaoException("error during book updating", ex);
        }
    }

    @Override
    public int deleteById(Long id) {
        Map<String, Object> params = Map.of(ID_FILED, id);
        try {
            return jdbcOperations.update("delete from books where id = :id", params);
        } catch (DataAccessException ex) {
            throw new BookDaoException("error during book deleting by id " + id, ex);
        }
    }

    private static class BooksMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            return Book.builder()
                    .id(resultSet.getLong(ID_FILED))
                    .title(resultSet.getString(TITLE_FILED))
                    .author(Author.builder()
                            .id(resultSet.getLong("author_id"))
                            .firstName(resultSet.getString("author_first_name"))
                            .lastName(resultSet.getString("author_last_name"))
                            .build())
                    .genre(Genre.builder()
                            .id(resultSet.getLong("genre_id"))
                            .title(resultSet.getString("genre_title"))
                            .build())
                    .build();
        }
    }
}
