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
import ru.otus.books.exceptions.AuthorDaoException;
import ru.otus.books.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

@Slf4j
@Repository
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AuthorDaoJdbc implements AuthorDao {

    private static final String ID_FILED = "id";

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Author getById(Long id) {
        try {
            return jdbcOperations.queryForObject("select * from authors where id = :id",
                    singletonMap(ID_FILED, id), new AuthorsMapper());
        } catch (DataAccessException ex) {
            throw new AuthorDaoException("error getting author by id " + id, ex);
        }
    }

    @Override
    public List<Author> getAll() {
        return jdbcOperations.query("select * from authors", new AuthorsMapper());
    }

    @Override
    public Long create(Author author) {
        SqlParameterSource params = new MapSqlParameterSource(Map.of(
                "firstName", author.getFirstName(),
                "lastName", author.getLastName()));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcOperations.update("insert into authors (`first_name`,`last_name`) values (:firstName, :lastName)",
                    params, keyHolder);
            return keyHolder.getKeyAs(Long.class);
        } catch (DataAccessException ex) {
            throw new AuthorDaoException("error during author creating", ex);
        }
    }

    @Override
    public int update(Long id, String firstName, String lastName) {
        Map<String, Object> params = Map.of(
                ID_FILED, id,
                "firstName", firstName,
                "lastName", lastName);
        try {
            return jdbcOperations.update("update authors set first_name = :firstName, last_name = :lastName where id = :id",
                    params);
        } catch (DataAccessException ex) {
            throw new AuthorDaoException("error during author updating", ex);
        }
    }

    @Override
    public int deleteById(Long id) {
        Map<String, Object> params = Map.of(ID_FILED, id);
        try {
            return jdbcOperations.update("delete from authors where id = :id", params);
        } catch (DataAccessException ex) {
            throw new AuthorDaoException("error during author deleting by id " + id, ex);
        }
    }

    private static class AuthorsMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            return Author.builder()
                    .id(resultSet.getLong(ID_FILED))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .build();
        }
    }
}
