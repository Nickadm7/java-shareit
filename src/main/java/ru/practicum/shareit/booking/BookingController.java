package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.utils.Utils;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final Utils utils;
    private final BookingService bookingService;
    private static final String OWNER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingAddDto bookingAddDto(@Valid @RequestBody BookingAddDto bookingAddDto, @RequestHeader(OWNER_ID) Long bookerId) {
        log.info("POST-запрос к эндпоинту /bookings владелец id: {}", bookerId);
        return bookingService.addBooking(bookingAddDto, bookerId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader(OWNER_ID) Long userId) {
        log.info("GET-запрос к эндпоинту /bookings пользователь id: {}", userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestParam(value = "state", required = false) State state,
                                                 @RequestHeader(OWNER_ID) Long userId) {
        log.info("GET-запрос к эндпоинту /bookings все бронирования пользователя id: {}", userId);
        return bookingService.getAllBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByOwner(@RequestParam(value = "state", required = false) State state,
                                                  @RequestHeader(OWNER_ID) Long ownerId) {
        log.info("GET-запрос к эндпоинту /bookings/owner все бронирования пользователя id: {}", ownerId);
        return bookingService.getAllBookingsByOwner(ownerId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingByOwner(@PathVariable Long bookingId
            , @RequestHeader(OWNER_ID) Long ownerId
            , @RequestParam Boolean approved) {
        log.info("POST-запрос к эндпоинту /bookings/ обновление бронирования id: {}", bookingId);
        return bookingService.updateBookingByOwner(bookingId, ownerId, approved);
    }
}