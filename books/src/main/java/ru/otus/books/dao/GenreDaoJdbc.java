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
import ru.otus.books.exceptions.GenreDaoException;
import ru.otus.books.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GenreDaoJdbc implements GenreDao {

    private static final String TITLE_FILED = "title";
    private static final String ID_FILED = "id";

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Genre getById(Long id) {
        try {
            return jdbcOperations.queryForObject("select id, title from genres where id = :id",
                    Map.of(ID_FILED, id), new GenreMapper());
        } catch (DataAccessException ex) {
            throw new GenreDaoException("error getting genre by id " + id, ex);
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbcOperations.query("select id, title from genres", new GenreMapper());
    }

    @Override
    public Long create(Genre genre) {
        SqlParameterSource params = new MapSqlParameterSource(Map.of(TITLE_FILED, genre.getTitle()));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcOperations.update("insert into genres (title) values :title", params, keyHolder);
            return keyHolder.getKeyAs(Long.class);
        } catch (DataAccessException ex) {
            throw new GenreDaoException("error during genre creating", ex);
        }
    }

    @Override
    public int update(Long id, String title) {
        Map<String, Object> params = Map.of(
                ID_FILED, id,
                TITLE_FILED, title);
        try {
            return jdbcOperations.update("update genres set title = :title where id = :id", params);
        } catch (DataAccessException ex) {
            throw new GenreDaoException("error during genre updating", ex);
        }
    }

    @Override
    public int deleteById(Long id) {
        Map<String, Object> params = Map.of(ID_FILED, id);
        try {
            return jdbcOperations.update("delete from genres where id = :id", params);
        } catch (DataAccessException ex) {
            throw new GenreDaoException("error during genre deleting by id " + id, ex);
        }
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            return Genre.builder()
                    .id(resultSet.getLong(ID_FILED))
                    .title(resultSet.getString(TITLE_FILED))
                    .build();
        }
    }
}
