package io.github.prittspadelord.application.data.dao;

import io.github.prittspadelord.application.data.models.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean checkUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = :username";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("username", username);

        try {
            return Boolean.TRUE.equals(this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, Boolean.class));
        }
        catch(EmptyResultDataAccessException e) {
            return false;
        }
    }

    //should we add another param to filter fields here?
    //it would limit the data fetched, but would increase complexity of the return object
    public User getUserFromUsername(String username) {
        String sql = "SELECT id, recent_password_update_timestamp, username, nickname, hashed_password FROM users WHERE username = :username";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("username", username);

        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

        return this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    public void insertUser(User user) {

        String sql = "INSERT INTO users (id, recent_password_update_timestamp, username, nickname, hashed_password) VALUES (:id, :recent_password_update_timestamp, :username, :nickname, :hashed_password)";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", user.getId())
            .addValue("username", user.getUsername())
            .addValue("nickname", user.getNickname())
            .addValue("hashed_password", user.getHashedPassword())
            .addValue("recent_password_update_timestamp", user.getRecentPasswordUpdateTimestamp());

        int rowsAffected = this.namedParameterJdbcTemplate.update(sql, parameterSource);

        log.info("Inserted user with id {} with {} rows affected", user.getId(), rowsAffected);
    }
}