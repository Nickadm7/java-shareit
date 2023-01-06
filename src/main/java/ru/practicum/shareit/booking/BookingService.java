package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingAddDto bookingAddDto, Long bookerId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getAllBookingsByUser(Long userId, State state);

    List<BookingDto> getAllBookingsByOwner(Long ownerId, State state);

    BookingDto updateBookingByOwner(Long bookingId, Long userId, Boolean approved);
}




