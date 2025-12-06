package io.github.prittspadelord.application.data.dao.impl;

import io.github.prittspadelord.application.data.dao.UserDao;
import io.github.prittspadelord.application.data.dbmodels.User;

import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class JdbcTemplateUserDao implements UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcTemplateUserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void insertUser(User user) {

        String sql = "INSER INTO users (id, username, nickname, hashed_password, recent_password_update_timestamp) VALUES (:id, :username, :nickname, :hashed_password, :recent_password_update_timestamp)";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", user.getId())
            .addValue("username", user.getUsername())
            .addValue("nickname", user.getNickname())
            .addValue("hashed_password", user.getHashedPassword())
            .addValue("recent_password_update_timestamp", user.getRecentPasswordUpdateTimestamp());

        int rowsAffected = namedParameterJdbcTemplate.update(sql, parameterSource);

        log.info("Inserted user with id {} with {} rows affected", user.getId(), rowsAffected);
    }
}