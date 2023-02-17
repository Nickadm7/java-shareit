package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public UserDto addUser(UserDto userDto) {
        if (userDto.getEmail() != null) {
            log.info("Пользователь успешно добавлен");
            return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
        } else {
            log.info("Не корректны email пользователя при добавлении");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.info("Пользователь не найден по id: {}", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            log.info("Пользователь найден по id: {}", userId);
            return UserMapper.toUserDto(userRepository.findById(userId).get());
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        if (userId == null) {
            log.info("Пользователь не найден по id");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (userDto.getId() == null) {
            userDto.setId(userId);
        }
        if (userDto.getName() == null) {
            userDto.setName(userRepository.findById(userId).get().getName());
        }
        if (userDto.getEmail() == null) {
            userDto.setEmail(userRepository.findById(userId).get().getEmail());
        } else {
            if (!checkUserEmail(userDto)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public boolean checkUserEmail(UserDto userDto) {
        for (User currentUser : userRepository.findAll()) {
            if (currentUser.getEmail().equals(userDto.getEmail())) {
                log.info("Проверка email: {} не прошла", userDto.getEmail());
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }
        log.info("Проверка email: {} прошла", userDto.getEmail());
        return true;
    }
}