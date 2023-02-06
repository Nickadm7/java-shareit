package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    private static final String OWNER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(OWNER_ID) long bookerId,
                                             @RequestBody @Valid BookingAddDto bookingAddDto) {
        log.info("POST-запрос к эндпоинту /bookings владелец id: {}", bookerId);
        return bookingClient.create(bookerId, bookingAddDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(OWNER_ID) long userId,
                                                 @PathVariable Long bookingId) {
        log.info("GET-запрос к эндпоинту /bookings bookingId: {} и userId: {}", bookingId, userId);
        return bookingClient.findById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByUser(@RequestHeader(OWNER_ID) long userId,
                                                       @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Optional<BookingState> state = BookingState.from(stateParam);
        if (state.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Unknown state: " + stateParam), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("GET-запрос к эндпоинту /bookings все бронирования пользователя id: {}", userId);
        return bookingClient.findAllForUser(userId, state.get(), from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader(OWNER_ID) Long userId,
                                                        @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        Optional<BookingState> state = BookingState.from(stateParam);
        if (state.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Unknown state: " + stateParam),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("GET-запрос к эндпоинту /bookings/owner все бронирования пользователя id: {}", userId);
        return bookingClient.findAllForOwner(userId, state.get(), from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setStatusBooking(@RequestHeader(OWNER_ID) Long userId,
                                                   @PathVariable Long bookingId,
                                                   @RequestParam(value = "approved") Boolean approved) {
        log.info("POST-запрос к эндпоинту /bookings/ обновление бронирования id: {}", bookingId);
        return bookingClient.setStatusBooking(userId, bookingId, approved);
    }
}