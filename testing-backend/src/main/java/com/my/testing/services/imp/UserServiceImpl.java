package com.my.testing.services.imp;

import com.my.testing.exceptions.PasswordMismatchException;
import com.my.testing.exceptions.UserAlreadyExistsException;
import com.my.testing.exceptions.UserNotFoundException;
import com.my.testing.models.User;
import com.my.testing.models.enums.Role;
import com.my.testing.repositories.UserRepository;
import com.my.testing.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("User with id '%s' was not found", id)));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email '%s' was not found", email)));
    }

    @Transactional
    @Override
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void changePassword(String email, String oldPass, String newPass) {
        User user = findByEmail(email);

        if (!passwordEncoder.matches(oldPass, user.getPassword()))
            throw new PasswordMismatchException("Old password doesn't match.");

        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void changeRole(String email, Role role) {
        User user = findByEmail(email);

        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public User save(User user) {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException(String.format("Email '%s' is already in use", user.getEmail()));
        }

        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(String oldEmail, User user) {
        User userToUpdate = findByEmail(oldEmail);

        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());

        return userRepository.save(userToUpdate);
    }
}
