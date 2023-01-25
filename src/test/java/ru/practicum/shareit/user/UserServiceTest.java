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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        verify(userRepository, times(1)).save(any(User.class));

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
        verify(userRepository, atLeast(2)).findById(anyLong());
    }

    @Test
    @DisplayName("Тест поиск пользователя по неверному id")
    void getUserByIdTestWrongIdTest() {
        Long userId = 0L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> userService.getUserById(userId));
        verify(userRepository, atLeast(0)).findById(anyLong());
    }

    @Test
    @DisplayName("Тест поиск всех пользователей")
    void getAllUsersTest() {
        User user = new User(1L, "userTestName", "mailtest@mail.ru");
        when(userRepository.findAll())
                .thenReturn(List.of(user));
        List<UserDto> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(user.getId(), users.get(0).getId());
        assertEquals(user.getName(), users.get(0).getName());
        assertEquals(user.getEmail(), users.get(0).getEmail());
        verify(userRepository, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Тест обновление пользователя")
    void updateUserTest() {
        when(userRepository.save(any(User.class)))
                .thenReturn(new User());

        userService.updateUser((new UserDto(1L, "userTestName", "mailtest@mail.ru")), 1L);

        verify(userRepository, times(1)).save(any());
    }



}