package com.my.testing.services;

import com.my.testing.models.User;

public interface UserService {
    User save(User user);
    User findById(Long id);
    User findByEmail(String email);

    void changeUserPassword(User user, String password);
}
