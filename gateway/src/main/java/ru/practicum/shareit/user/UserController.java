package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("GET-запрос к эндпоинту /users список всех пользователей");
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("GET-запрос к эндпоинту /users найти пользователя по id: {}", userId);
        return userClient.findById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST-запрос к эндпоинту /users пользователь с name: {} и email: {}", userDto.getName(), userDto.getEmail());
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("PATCH-запрос к эндпоинту /users обновить пользователя по id: {}", userId);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(@PathVariable Long userId) {
        log.info("DELETE-запрос к эндпоинту /users удалить пользователя с id: {}", userId);
        return userClient.deleteById(userId);
    }
}