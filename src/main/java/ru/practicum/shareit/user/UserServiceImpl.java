package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        if (userDto.getEmail() != null) {
            return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (userRepository.findById(userId).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (userDto.getId() == null) {
            userDto.setId(userId);
        }
        if (userDto.getName() == null) {
            userDto.setName(userRepository.findById(userId).get().getName()); //TODO isPresent
        }
        if (userDto.getEmail() == null) {
            userDto.setEmail(userRepository.findById(userId).get().getEmail()); //TODO isPresent
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
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }
        return true;
    }
}