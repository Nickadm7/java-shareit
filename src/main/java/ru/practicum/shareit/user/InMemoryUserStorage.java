package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userDbStorage;
    private Long id;

    public InMemoryUserStorage() {
        userDbStorage = new HashMap<>();
        id = 0L;
    }

    @Override
    public User addUser(User user) {
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (checkUserEmail(user)) {
            if (user.getId() == null) {
                user.setId(generateUserId());
            }
            userDbStorage.put(user.getId(), user);
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @Override
    public User getUserById(Long userId) {
        if (userDbStorage.containsKey(userId)) {
            return userDbStorage.get(userId);
        } else {
            throw new IllegalArgumentException("user с id: " + userId + " не найден!");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return userDbStorage.values();
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!userDbStorage.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (user.getName() == null) {
            user.setName(userDbStorage.get(user.getId()).getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userDbStorage.get(user.getId()).getEmail());
        } else {
            if (!checkUserEmail(user)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }
        userDbStorage.put(user.getId(), user);
        return userDbStorage.get(user.getId());
    }

    @Override
    public void deleteUserById(Long userId) {
        if (userDbStorage.containsKey(userId)) {
            userDbStorage.remove(userId);
        } else {
            throw new IllegalArgumentException("user с id: " + userId + " не найден!");
        }
    }

    public boolean checkUserEmail(User user) {
        for (User currentUser : userDbStorage.values()) {
            if (currentUser.getEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }
        return true;
    }

    public Long generateUserId() {
        return ++id;
    }
}
