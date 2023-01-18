package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.State;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final String OWNER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingAddDto bookingAddDto, @RequestHeader(OWNER_ID) Long bookerId) {
        log.info("POST-запрос к эндпоинту /bookings владелец id: {}", bookerId);
        return bookingService.addBooking(bookingAddDto, bookerId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader(OWNER_ID) Long userId) {
        log.info("GET-запрос к эндпоинту /bookings bookingId: {} и userId: {}", bookingId, userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestParam(value = "state", required = false) State state,
                                                 @RequestHeader(OWNER_ID) Long userId,
                                                 @RequestParam(name = "from", required = false) Integer from,
                                                 @RequestParam(name = "size", required = false) Integer size) {
        log.info("GET-запрос к эндпоинту /bookings все бронирования пользователя id: {}", userId);
        return bookingService.getAllBookingsByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByOwner(@RequestParam(value = "state", required = false) State state,
                                                  @RequestHeader(OWNER_ID) Long ownerId,
                                                  @RequestParam(name = "from", required = false) Integer from,
                                                  @RequestParam(name = "size", required = false) Integer size) {
        log.info("GET-запрос к эндпоинту /bookings/owner все бронирования пользователя id: {}", ownerId);
        return bookingService.getAllBookingsByOwner(ownerId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingByOwner(@PathVariable Long bookingId,
                                           @RequestHeader(OWNER_ID) Long userId,
                                           @RequestParam Boolean approved) {
        log.info("POST-запрос к эндпоинту /bookings/ обновление бронирования id: {}", bookingId);
        return bookingService.updateBookingByOwner(bookingId, userId, approved);
    }
}