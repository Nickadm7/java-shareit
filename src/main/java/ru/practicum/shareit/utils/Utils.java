package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class Utils {
    private final UserService userService;
    private final UserRepository userRepository;

    public boolean isUserExist(Long ownerId) {
        return userService.getUserById(ownerId) != null;
    }

    public User getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }
}