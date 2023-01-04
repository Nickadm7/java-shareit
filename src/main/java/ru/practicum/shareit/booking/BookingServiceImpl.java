package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.utils.Utils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final Utils utils;

    @Override
    public BookingDto addBooking(BookingAddDto bookingAddDto, Long bookerId) {
        if (!utils.isUserExist(bookerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!utils.isItemExist(bookingAddDto.getItemId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!utils.isItemAvailable(bookingAddDto.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return BookingMapper.toBookingDto(bookingRepository
                .save(BookingMapper.toAddBooking(bookingAddDto
                        , utils.getUserById(bookerId)
                        , utils.getItemById(bookingAddDto.getItemId()))));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get()); //TODO
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, State state) {
        return null;
    }

    @Override
    public List<BookingDto> getAllBookingsByOwner(Long ownerId, State state) {
        return null;
    }

    @Override
    public BookingDto updateBookingByOwner(Long bookingId, Long ownerId, Boolean approved) {
        return null;
    }
}
