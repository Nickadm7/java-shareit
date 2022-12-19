package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return userMapper.toUserDto(userStorage.addUser(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.toUserDto(userStorage.getUserById(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        if (userDto.getId() == null) {
            userDto.setId(userId);
        }
        return userMapper.toUserDto(userStorage.updateUser(userMapper.toUser(userDto)));
    }

    @Override
    public void deleteById(Long userId) {
        userStorage.deleteUserById(userId);
    }
}