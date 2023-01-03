package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto, Long requestId);
}
