package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UtilsTest {
    private final Utils utils;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final BookingService bookingService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private User user = new User(1L, "testName", "testemail@mail.ru");
    private User user2 = new User(2L, "testName2", "testemail@mail.ru");
    private Item item = new Item(1L, "itemName", "itemDescription", true, user, null);

    @Test
    @DisplayName("Тест получение пользователя по id")
    void getUserByIdTest() {
        UserDto newUserDto = userService.addUser(userMapper.toUserDto(user));
        User actual = utils.getUserById(newUserDto.getId());
        assertEquals("testName", actual.getName());
    }

    @Test
    @DisplayName("Тест получение вещи по id не найден владелец")
    void getItemByIdTest() {
        Item actual = utils.getItemById(99L);

        assertThrows(EntityNotFoundException.class,
                () -> actual.equals(null));
    }

    @Test
    @DisplayName("Тест проверка существования вещи по id")
    void isItemExistTest() {
        UserDto newUserDto = userService.addUser(userMapper.toUserDto(user));
        ItemDto newItemDto = itemService.addItem(ItemMapper.toItemDto(item), 1L);
        utils.isItemExist(1L);
    }

    @Test
    @DisplayName("Тест получение вещи по неправильному id")
    void isItemExistWrongIdTest() {
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> utils.isItemExist(99L));
    }
}
