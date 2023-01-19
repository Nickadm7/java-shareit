package ru.practicum.shareit.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Тест добавление нового пользователя")
    void addUserTest() {
        User userToSave = new User(1L, "userTestName", "mailtest@mail.ru");
        UserDto userToSaveDto = new UserDto(1L, "userTestName", "mailtest@mail.ru");
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        UserDto actualUserDto = userService.addUser(userToSaveDto);

        assertEquals(userToSaveDto, actualUserDto);

    }

    @Test
    @DisplayName("Тест добавление нового пользователя неверный email")
    void addUserWrongEmailTest() {
        UserDto userToSaveDto = new UserDto(1L, "userTestName", "");

        when(userRepository.save(any(User.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(ResponseStatusException.class,
                () -> userService.addUser(userToSaveDto));
    }

    @Test
    @DisplayName("Тест поиск пользователя по id")
    void getUserByIdTest() {
        Long userId = 1L;
        User expected = new User(1L, "userTestName", "mailtest@mail.ru");
        UserDto expectedUser = new UserDto(1L, "userTestName", "mailtest@mail.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(expected));

        UserDto actualUser = userService.getUserById(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("Тест поиск пользователя по неверному id")
    void getUserByIdTestWrongId() {
        Long userId = 0L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> userService.getUserById(userId));
    }
}