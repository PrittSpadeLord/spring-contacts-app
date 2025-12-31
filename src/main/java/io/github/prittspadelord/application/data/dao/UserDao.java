package io.github.prittspadelord.application.data.dao;

import io.github.prittspadelord.application.data.models.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        String sql = "SELECT count(id) FROM users WHERE username = :username";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("username", username);

        Integer count = this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, Integer.class);

        return count != null && count > 0;
    }

    public User getUserFromId(long id) {
        String sql = """
            SELECT
                id, authorization_level, recent_password_update_timestamp, username, nickname, hashed_password
            FROM users
            WHERE id = :id
            """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

        return this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    public User getUserFromUsername(String username) {
        String sql = """
            SELECT
                id, authorization_level, recent_password_update_timestamp, username, nickname, hashed_password
            FROM users
            WHERE username = :username
            """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("username", username);

        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

        return this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    public void insertUser(User user) {

        String sql = """
            INSERT INTO users (
                id,
                authorization_level,
                recent_password_update_timestamp,
                username,
                nickname,
                hashed_password
            )
            VALUES (
                :id,
                :authorization_level::authorization_level,
                :recent_password_update_timestamp,
                :username,
                :nickname,
                :hashed_password
            )
            """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", user.getId())
            .addValue("authorization_level", user.getAuthorizationLevel().name())
            .addValue("recent_password_update_timestamp", user.getRecentPasswordUpdateTimestamp())
            .addValue("username", user.getUsername())
            .addValue("nickname", user.getNickname())
            .addValue("hashed_password", user.getHashedPassword());

        int rowsAffected = this.namedParameterJdbcTemplate.update(sql, parameterSource);

        log.info("Inserted user with id {} with {} rows affected", user.getId(), rowsAffected);
    }
}