package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.utils.Utils;

import javax.validation.Valid;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final Utils utils;
    private final BookingService bookingService;
    private static final String OWNER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingAddDto bookingAddDto, @RequestHeader(OWNER_ID) Long bookerId) {
        log.info("POST-запрос к эндпоинту /bookings владелец id: {}", bookerId);
        if (utils.isUserExist(bookerId)) {
            return bookingService.addBooking(bookingAddDto, bookerId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}