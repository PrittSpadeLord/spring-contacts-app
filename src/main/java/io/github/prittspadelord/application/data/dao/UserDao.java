package io.github.prittspadelord.application.data.dao;

import io.github.prittspadelord.application.data.dbmodels.User;

public interface UserDao {
    void insertUser(User user);
}