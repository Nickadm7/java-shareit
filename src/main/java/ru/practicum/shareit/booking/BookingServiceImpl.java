package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.utils.Utils;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final Utils utils;

    @Override
    public BookingDto addBooking(BookingAddDto bookingAddDto, Long requestId) {
        if (!utils.isUserExist(requestId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (utils.isItemExist(bookingAddDto.getItemId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!utils.isItemAvailable(bookingAddDto.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return BookingMapper.toBookingDto(bookingRepository
                .save(BookingMapper.toAddBooking(bookingAddDto, utils.getUserById(requestId), utils.getItemById(bookingAddDto.getItemId()))));

    }
}
