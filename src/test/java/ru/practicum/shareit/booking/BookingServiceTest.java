package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemOutForFindDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Utils;

import java.time.LocalDateTime;

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
}
