package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.utils.Utils;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final Utils utils;

    @Override
    public BookingDto addBooking(BookingDto bookingDto, Long requestId) {
        if (utils.isUserExist(requestId)) {
            return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(bookingDto, utils.getUserById(requestId))));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
