package com.my.testing.services;

import com.my.testing.models.User;
import com.my.testing.models.enums.Role;

public interface UserService {
    User save(User user);
    User findById(Long id);
    User findByEmail(String email);
    User update(String oldEmail, User user);
    void changePassword(User user, String password);
    void changePassword(String email, String oldPass, String newPass);
    void changeRole(String email, Role role);
}
