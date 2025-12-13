package io.github.prittspadelord.application.data.dao;

import io.github.prittspadelord.application.data.models.User;

public interface UserDao {
    boolean checkUsername(String username);
    void insertUser(User user);
}