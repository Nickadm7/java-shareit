package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userStorage.addUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(userStorage.getUserById(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        if (userDto.getId() == null) {
            userDto.setId(userId);
        }
        return UserMapper.toUserDto(userStorage.updateUser(UserMapper.toUser(userDto)));
    }

    @Override
    public void deleteById(Long userId) {
        userStorage.deleteUserById(userId);
    }
}