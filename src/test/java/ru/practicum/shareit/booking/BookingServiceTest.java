package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemOutForFindDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingServiceTest {
    private BookingServiceImpl bookingService;
    private BookingRepository bookingRepository;
    private ItemService itemService;
    private UserService userService;
    private Utils utils;
    User user;
    User owner;
    Item item;
    BookingDto bookingDto;
    Booking booking;
    ItemOutForFindDto itemOutForFindDto;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        itemService = mock(ItemService.class);
        userService = mock(UserService.class);
        utils = mock(Utils.class);
        bookingService = new BookingServiceImpl(bookingRepository, utils);

        user = new User(1L, "userTestName", "mailtest@mail.ru");
        owner = new User(2L, "ownerTestName", "ownermailtest@mail.ru");
        item = new Item(1L,
                "ItemTestName",
                "TestDescription",
                true,
                owner,
                null);
        LocalDateTime start = LocalDateTime.parse("2222-10-12T14:00");
        LocalDateTime end = LocalDateTime.parse("2222-12-12T14:00");
        bookingDto = new BookingDto(1L,
                start,
                end,
                item,
                user,
                Status.APPROVED
        );
        itemOutForFindDto = new ItemOutForFindDto(1L,
                "ItemTestName",
                "TestDescription",
                true,
                null,
                null,
                null);
        booking = new Booking(1L,
                start,
                end,
                item,
                user,
                Status.APPROVED
        );
    }

    @Test
    @DisplayName("Тест добавление бронирования для несуществующей вещи")
    void addBookingWrongItemExistTest() {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemOutForFindDto);
        when(userService.getUserById(user.getId())).thenReturn(UserMapper.toUserDto(user));
        when(bookingRepository.save(any())).thenReturn(booking);

        when(bookingRepository.save(booking))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class,
                () -> bookingService.addBooking(BookingMapper.toAddBookingDto(booking), 1L));
    }

    @Test
    @DisplayName("Тест добавление бронирования для несуществующей вещи")
    void addBookingWrongItemNotAvailableTest() {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemOutForFindDto);
        when(userService.getUserById(user.getId())).thenReturn(UserMapper.toUserDto(user));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(utils.isItemExist(anyLong())).thenReturn(true);

        when(bookingRepository.save(booking))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(ResponseStatusException.class,
                () -> bookingService.addBooking(BookingMapper.toAddBookingDto(booking), 1L));
        verify(bookingRepository, times(0)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Тест добавление бронирования")
    void addBookingTest() {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemOutForFindDto);
        when(userService.getUserById(user.getId())).thenReturn(UserMapper.toUserDto(user));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(utils.isItemExist(anyLong())).thenReturn(true);
        when(utils.isItemAvailable(anyLong())).thenReturn(true);
        when(utils.getItemById(anyLong())).thenReturn(item);

        BookingDto actualBooking = bookingService.addBooking(BookingMapper.toAddBookingDto(booking), 1L);

        assertNotNull(actualBooking);
        assertEquals(bookingDto.getStart(), actualBooking.getStart());
        assertEquals(bookingDto.getEnd(), actualBooking.getEnd());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Тест поиск бронирования по непровильному id")
    void getBookingByWrongIdTest() {
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        verify(bookingRepository, times(0)).findById(anyLong());
    }

    @Test
    @DisplayName("Тест поиск бронирования по id")
    void getBookingByIdTest() {
        Long bookingId = 1L;
        Long userId = 1L;
        when(utils.isBookingExistById(bookingId)).thenReturn(true);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingDto actualBooking = bookingService.getBookingById(bookingId, userId);

        assertEquals(bookingDto, actualBooking);
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Тест все бронирования пользователя неправильная пагинация 0 и 0")
    void getAllBookingsByUserWrongPaginationTest() {
        assertThrows(ResponseStatusException.class,
                () -> bookingService.getAllBookingsByUser(1L, State.CURRENT, 0, 0));
    }

    @Test
    @DisplayName("Тест все бронирования пользователя неправильная пагинация -1 и 0")
    void getAllBookingsByUserWrongPaginationTest1() {
        assertThrows(ResponseStatusException.class,
                () -> bookingService.getAllBookingsByUser(1L, State.CURRENT, -1, 0));
    }

    @Test
    @DisplayName("Тест все бронирования владельца неправильная пагинация 0 и 0")
    void getAllBookingsByOwnerTest() {
        assertThrows(ResponseStatusException.class,
                () -> bookingService.getAllBookingsByOwner(1L, State.CURRENT, 0, 0));
    }

    @Test
    @DisplayName("Тест все бронирования владельца неправильная пагинация 0 и 0")
    void getAllBookingsByOwnerTest1() {
        assertThrows(ResponseStatusException.class,
                () -> bookingService.getAllBookingsByOwner(1L, State.CURRENT, -1, 0));
    }

    @Test
    @DisplayName("Тест все бронирования владельца статус ALL")
    void getAllBookingsByOwner_ALL_Test() {
        List<Booking> bufferBooking = List.of(booking);
        when(bookingRepository.findByItem_OwnerId_OrderByEndDesc(anyLong(), any()))
                .thenReturn(bufferBooking);

        List<BookingDto> actual = bookingService.getAllBookingsByOwner(1L, State.ALL, 1, 2);

        assertNotNull(actual);
        assertEquals(booking.getId(), actual.get(0).getId());
        assertEquals(booking.getStatus(), actual.get(0).getStatus());
        assertEquals(booking.getStart(), actual.get(0).getStart());
    }
}
