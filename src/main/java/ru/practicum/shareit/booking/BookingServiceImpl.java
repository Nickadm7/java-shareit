package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.State.ALL;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final Utils utils;

    @Override
    public BookingAddDto addBooking(BookingAddDto bookingAddDto, Long bookerId) {
        if (!utils.isUserExist(bookerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
        if (!utils.isItemExist(bookingAddDto.getItemId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //вещь не существует
        }
        /*
        if (!utils.isItemAvailable(bookingAddDto.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //вещь не доступна для бронирования
        }
        if (bookingAddDto.getStart().isAfter(bookingAddDto.getEnd())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //старт бронирования после окончания
        }
        if (utils.getItemById(bookingAddDto.getItemId()).getOwner().getId().equals(bookerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //бронировать свою вещь нельзя
        }

         */
        Booking booking = BookingMapper.toBooking(bookingAddDto
                , utils.getItemById(bookingAddDto.getItemId())
                , utils.getUserById(bookerId));
        bookingRepository.save(booking);
        return BookingMapper.toAddBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        if (!utils.isBookingExistById(bookingId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //бронирование не существует
        }
        if (!utils.isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
        return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get()); //TODO
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, State state) {
        if (!utils.isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
        return null;
    }

    @Override
    public List<BookingDto> getAllBookingsByOwner(Long ownerId, State state) {
        if (!utils.isUserExist(ownerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (state == null) {
            state = ALL;
        }
        List<Booking> bufferOutBooking = new ArrayList<>();
        switch (state) {
            case ALL:
                bufferOutBooking = bookingRepository.findAllBookingsBybookerIdOrderByEndDesc(ownerId);
                break;
            case WAITING:
                break;
        }
        return converterBookingToDto(bufferOutBooking);
    }

    @Override
    public BookingDto updateBookingByOwner(Long bookingId, Long ownerId, Boolean approved) {
        return null;
    }

    private List<BookingDto> converterBookingToDto(List<Booking> bufferOutBooking) {
        List<BookingDto> outBookingDto = new ArrayList<>();
        return bufferOutBooking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
