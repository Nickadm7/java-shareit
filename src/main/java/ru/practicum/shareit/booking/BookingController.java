package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    public BookingDto addBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader(OWNER_ID) Long requestId) {
        log.info("POST-запрос к эндпоинту /bookings request_id: {}", requestId);
        if (utils.isUserExist(requestId)) {
            return bookingService.addBooking(bookingDto, requestId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}