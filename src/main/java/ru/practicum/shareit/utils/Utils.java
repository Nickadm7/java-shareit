package ru.practicum.shareit.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

@Service
public class Utils {
    private final UserService userService;

    @Autowired
    public Utils(UserService userService, ItemService itemService) {
        this.userService = userService;
    }

    public boolean isUserExist(Long ownerId) {
        return userService.getUserById(ownerId) != null;
    }
}