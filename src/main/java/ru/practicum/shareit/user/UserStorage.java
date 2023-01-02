package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    User addUser(User user);

    Collection<User> getAllUsers();

    User getUserById(Long userId);

    void deleteUserById(Long userId);

    User updateUser(User user);
}