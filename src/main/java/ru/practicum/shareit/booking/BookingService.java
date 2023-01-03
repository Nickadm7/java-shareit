package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto addBooking(BookingAddDto bookingAddDto, Long requestId);
}
