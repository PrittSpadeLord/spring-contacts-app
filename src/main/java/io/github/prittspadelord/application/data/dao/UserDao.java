package io.github.prittspadelord.application.data.dao;

import io.github.prittspadelord.application.data.models.User;
import io.github.prittspadelord.application.support.AuthorizationLevel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean checkUsername(String username) {
        String sql = this.sqlFromFile("select_idcount_where_username.sql");

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("username", username);

        Integer count = this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, Integer.class);

        return count != null && count > 0;
    }

    public AuthorizationLevel getAuthorizationLevelForId(long id) {
        // in the future we will run an unlogged postgresql table query or valkey query before we call the db like this
        String sql = this.sqlFromFile("select_auth_where_id.sql");

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, AuthorizationLevel.class);
    }

    public long getPSTForId(long id) {
        // in the future we will run an unlogged postgresql table query or valkey query before we call the db like this
        String sql = this.sqlFromFile("select_pst_where_id.sql");

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return Objects.requireNonNull(this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, Long.class));
    }

    public User getUserFromUsername(String username) {
        String sql = this.sqlFromFile("select_user_where_username.sql");

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("username", username);

        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

        return this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    public void insertUser(User user) {
        String sql = this.sqlFromFile("insert_into_users.sql");

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

    private String sqlFromFile(String fileUrlString) {
        InputStream sqlStream = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/users/" + fileUrlString));

        BufferedReader reader = new BufferedReader(new InputStreamReader(sqlStream));

        StringBuilder sb = new StringBuilder();

        int character;

        try {
            while((character = reader.read()) != -1) {
                sb.append((char) character);
            }

            return sb.toString();
        }
        catch(IOException e) {
            log.error("Critical failure (edit this later)");
            throw new RuntimeException(e);
        }
    }
}