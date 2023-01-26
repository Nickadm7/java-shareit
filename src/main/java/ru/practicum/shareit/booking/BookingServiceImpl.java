package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
        utils.isUserExist(bookerId);
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
        Booking booking = BookingMapper.toBooking(bookingAddDto,
                utils.getItemById(bookingAddDto.getItemId()),
                utils.getUserById(bookerId));
        bookingRepository.save(booking);
        return BookingMapper.toBookingAddDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        if (!utils.isBookingExistById(bookingId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //бронирование не существует
        }
        utils.isUserExist(userId);
        utils.checkItemsToOwner(bookingId, userId);
        return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get());
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, State state, Integer from, Integer size) {
        utils.isUserExist(userId);
        if (state == null) {
            state = State.ALL;
        }
        List<Booking> bufferAllBookings;
        if (from == null || size == null) {
            bufferAllBookings = bookingRepository.findAllBookings_BybookerId_OrderByEndDesc(userId);

        } else {
            if (from == 0 && size == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //не корректные параметры пагинации
            }
            if (from < 0 || size < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //не корректные параметры пагинации
            }
            PageRequest pageRequest = PageRequest.of(from / size, size);
            bufferAllBookings = bookingRepository.findAllBookings_BybookerId_OrderByEndDesc(userId, pageRequest);
        }
        return sortByStateForUser(bufferAllBookings, state, userId);
    }

    @Override
    public List<BookingDto> getAllBookingsByOwner(Long ownerId, State state, Integer from, Integer size) {
        utils.isUserExist(ownerId); //проверка существует ли пользователь
        if (state == null) {
            state = ALL;
        }
        List<Booking> bufferAllBookings;
        if (from == null || size == null) {
            bufferAllBookings = bookingRepository.findByItem_OwnerId_OrderByEndDesc(ownerId);
        } else {
            if (from == 0 && size == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //не корректные параметры пагинации
            }
            if (from < 0 || size < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //не корректные параметры пагинации
            }
            PageRequest pageRequest = PageRequest.of(from / size, size);
            bufferAllBookings = bookingRepository.findByItem_OwnerId_OrderByEndDesc(ownerId, pageRequest);
        }
        return sortByStateForOwner(bufferAllBookings, state, ownerId);
    }

    @Override
    public BookingDto updateBookingByOwner(Long bookingId, Long userId, Boolean approved) {
        if (!utils.isBookingExistById(bookingId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //бронирование не существует
        }
        utils.isUserExist(userId);
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
        Booking booking = bookingRepository.findById(bookingId).get();
        if (approved.equals(Boolean.TRUE)) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    public List<BookingDto> sortByStateForUser(List<Booking> bufferAllBookings, State state, Long userId) {
        List<Booking> outBookings = new ArrayList<>();
        switch (state) {
            case ALL:
                outBookings = bufferAllBookings;
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
                outBookings = bookingRepository.findAllBookings_ByBookerIdAndStatus_OrderByEndDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                outBookings = bookingRepository.findAllBookings_ByBookerIdAndStatus_OrderByEndDesc(userId, Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: " + state); //такого статуса нет
        }
        return converterBookingToDto(outBookings);
    }

    public List<BookingDto> sortByStateForOwner(List<Booking> bufferAllBookings, State state, Long ownerId) {
        List<Booking> outBookings = new ArrayList<>();
        switch (state) {
            case ALL:
                outBookings = bufferAllBookings;
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
                outBookings = bookingRepository.findByItem_OwnerIdAndStatus_OrderByEndDesc(ownerId, Status.WAITING);
                break;
            case REJECTED:
                outBookings = bookingRepository.findByItem_OwnerIdAndStatus_OrderByEndDesc(ownerId, Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: " + state); //такого статуса нет
        }
        return converterBookingToDto(outBookings);
    }

    private List<BookingDto> converterBookingToDto(List<Booking> bufferOutBooking) {
        return bufferOutBooking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
