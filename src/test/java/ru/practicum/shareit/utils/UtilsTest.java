package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class UtilsTest {

    private final UserService mockUserService = Mockito.mock(UserServiceImpl.class);
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
    private final BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
    private final ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
    private Utils utils;

    Booking bookingForTest;
    Item item;

    @BeforeEach
    void initItemService() {
        utils = new Utils(mockUserService,
                mockUserRepository,
                mockItemRepository,
                mockBookingRepository,
                mockItemRequestRepository);
    }

    @BeforeEach
    void initUser() {
        User user = new User(1L, "userTestName", "mailtest@mail.ru");
        User owner = new User(2L, "ownerTestName", "ownermailtest@mail.ru");
        item = new Item(1L,
                "ItemTestName",
                "TestDescription",
                true,
                owner,
                null);
        LocalDateTime start = LocalDateTime.parse("2222-10-12T14:00");
        LocalDateTime end = LocalDateTime.parse("2222-12-12T14:00");
        bookingForTest = new Booking(1L,
                start,
                end,
                item,
                owner,
                Status.APPROVED
        );
    }

    @Test
    @DisplayName("Тест isUserExist не существует пользователь")
    void isUserExistTest() {
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class,
                () -> utils.isUserExist(99L));

        Assertions.assertEquals("404 NOT_FOUND", thrown.getMessage());
    }

    @Test
    @DisplayName("Тест пользователь не владелец вещи")
    void checkItemsToOwnerTest() {
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookingForTest));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class,
                () -> utils.checkItemsToOwner(99L, 99L));

        Assertions.assertEquals("404 NOT_FOUND", thrown.getMessage());
    }

    @Test
    @DisplayName("Тест получение последнего бронирования")
    void getLastBookingTest() {
        Mockito
                .when(mockBookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(anyLong(), any()))
                .thenReturn(bookingForTest);

        BookingOutDto actual = utils.getLastBooking(1L);

        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    @DisplayName("Тест получение следующего бронирования")
    void getNextBookingTest() {
        Mockito
                .when(mockBookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(anyLong(), any()))
                .thenReturn(bookingForTest);

        BookingOutDto actual = utils.getNextBooking(1L);

        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    @DisplayName("Тест получение всех бронирований по BookerId и ItemId")
    void findAllBookingsByBookerIdAndItemIdTest() {
        Mockito
                .when(mockBookingRepository.findAllBookingsByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(List.of(bookingForTest));

        List<Booking> actual = utils.findAllBookingsByBookerIdAndItemId(1L, 1L);

        Assertions.assertEquals(1L, actual.get(0).getId());
    }

    @Test
    @DisplayName("Тест существует ли вещь")
    void isItemAvailableTest() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        boolean actual = utils.isItemAvailable(1L);

        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("Тест существует ли бронирование")
    void isBookingExistByIdTest() {
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookingForTest));

        boolean actual = utils.isBookingExistById(1L);

        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("Тест поиск вещи по запросу")
    void findItemsByRequestIdTest() {
        Mockito
                .when(mockItemRepository.findItemsByItemRequestId(anyLong()))
                .thenReturn(List.of(item));

        List<Item> actual = utils.findItemsByRequestId(1L);

        Assertions.assertEquals(1, actual.get(0).getId());
    }

    @Test
    @DisplayName("Тест поиск запроса по null id")
    void getItemRequestByNullIdTest() {
        Long itemRequestId = null;

        ItemRequest actual = utils.getItemRequestById(itemRequestId);

        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Тест поиск запроса по id")
    void getItemRequestByIdTest() {
        ItemRequest itemRequest = new ItemRequest(
                1L,
                "testDescription",
                new User(),
                LocalDateTime.now());
        Mockito
                .when(mockItemRequestRepository.getReferenceById(anyLong()))
                .thenReturn((itemRequest));

        ItemRequest actual = utils.getItemRequestById(1L);

        Assertions.assertEquals(1, actual.getId());
    }
}