package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.State.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final Utils utils;

    @Override
    public BookingDto addBooking(BookingAddDto bookingAddDto, Long bookerId) {
        if (!utils.isUserExist(bookerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
        if (!utils.isItemExist(bookingAddDto.getItemId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //вещь не существует
        }
        if (!utils.isItemAvailable(bookingAddDto.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //вещь не доступна для бронирования
        }
        if (bookingAddDto.getStart().isAfter(bookingAddDto.getEnd())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //старт бронирования после окончания
        }
        if (utils.getItemById(bookingAddDto.getItemId()).getOwner().getId().equals(bookerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //бронировать свою вещь нельзя
        }
        Booking booking = BookingMapper.toBooking(bookingAddDto
                , utils.getItemById(bookingAddDto.getItemId())
                , utils.getUserById(bookerId));
        bookingRepository.save(booking);
        return BookingMapper.toBookingAddDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        if (!utils.isBookingExistById(bookingId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //бронирование не существует
        }
        if (!utils.isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
        utils.checkItemsToOwner(bookingId, userId);
        return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get()); //TODO
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, State state) {
        if (!utils.isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
        if (state == null) {
            state = State.ALL;
        }
        List<Booking> outBookings = new ArrayList<>();
        List<Booking> bufferAllBookings = bookingRepository.findAllBookingsBybookerIdOrderByEndDesc(userId);
        switch (state) {
            case ALL:
                outBookings = bookingRepository.findAllBookingsBybookerIdOrderByEndDesc(userId);
                break;
            case CURRENT:
                for (Booking booking : bufferAllBookings) {
                    if (booking.getStart().isBefore(LocalDateTime.now())
                            && booking.getEnd().isAfter(LocalDateTime.now())) {
                        outBookings.add(booking);
                    }
                }
                break;
            case PAST:
                for (Booking booking : bufferAllBookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        outBookings.add(booking);
                    }
                }
                break;
            case FUTURE:
                for (Booking booking : bufferAllBookings) {
                    if (booking.getEnd().isAfter(LocalDateTime.now())) {
                        outBookings.add(booking);
                    }
                }
                break;
            case WAITING:
                outBookings = bookingRepository.findAllBookingsByBookerIdAndStatusOrderByEndDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                outBookings = bookingRepository.findAllBookingsByBookerIdAndStatusOrderByEndDesc(userId, Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: " + state); //такого статуса нет
        }
        return converterBookingToDto(outBookings);
    }

    @Override
    public List<BookingDto> getAllBookingsByOwner(Long ownerId, State state) {
        if (!utils.isUserExist(ownerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (state == null) {
            state = ALL;
        }
        List<Booking> outBookings = new ArrayList<>();
        List<Booking> bufferAllBookings = bookingRepository.findByItem_Owner_Id_OrderByEndDesc(ownerId);
        switch (state) {
            case ALL:
                outBookings = bookingRepository.findByItem_Owner_Id_OrderByEndDesc(ownerId);
                break;
            case CURRENT:
                for (Booking booking : bufferAllBookings) {
                    if (booking.getStart().isBefore(LocalDateTime.now())
                            && booking.getEnd().isAfter(LocalDateTime.now())) {
                        outBookings.add(booking);
                    }
                }
                break;
            case PAST:
                for (Booking booking : bufferAllBookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        outBookings.add(booking);
                    }
                }
                break;
            case FUTURE:
                for (Booking booking : bufferAllBookings) {
                    if (booking.getEnd().isAfter(LocalDateTime.now())) {
                        outBookings.add(booking);
                    }
                }
                break;
            case WAITING:
                outBookings = bookingRepository.findByItem_Owner_Id_AndStatusOrderByEndDesc(ownerId, Status.WAITING);
                break;
            case REJECTED:
                outBookings = bookingRepository.findByItem_Owner_Id_AndStatusOrderByEndDesc(ownerId, Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: " + state); //такого статуса нет
        }
        return converterBookingToDto(outBookings);
    }

    @Override
    public BookingDto updateBookingByOwner(Long bookingId, Long userId, Boolean approved) {
        if (!utils.isBookingExistById(bookingId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //бронирование не существует
        }
        if (!utils.isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //пользователь не существует
        }
        if (approved == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //статус неверно передан
        }
        Long ownerId = utils.getItemById(bookingRepository.findById(bookingId).get().getItem().getId())
                .getOwner().getId();
        if (!ownerId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не владелец вещи
        }
        if (bookingRepository.findById(bookingId).get().getStatus().equals(Status.APPROVED)
                && approved.equals(Boolean.TRUE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //бронирование уже подтверждено
        }
        Booking booking = bookingRepository.findById(bookingId).get(); //TODO
        if (approved.equals(Boolean.TRUE)) {
            booking.setStatus(Status.APPROVED);
            bookingRepository.save(booking);
        } else {
            booking.setStatus(Status.REJECTED);
            bookingRepository.save(booking);
        }
        return BookingMapper.toBookingDto(booking);
    }

    private List<BookingDto> converterBookingToDto(List<Booking> bufferOutBooking) {
        return bufferOutBooking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

}
