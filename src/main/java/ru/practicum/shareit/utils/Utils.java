package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Utils {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    public boolean isUserExist(Long ownerId) {
        return userService.getUserById(ownerId) != null;
    }

    public User getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }

    public Item getItemById(Long itemId) {
        return itemRepository.getReferenceById(itemId);
    }

    public ItemRequest getItemRequestById(Long itemRequestId) {
        if (itemRequestId != null) {
            return itemRequestRepository.getReferenceById(itemRequestId);
        } else {
            return null;
        }
    }

    public List<Item> findItemsByRequestId(Long requestId) {
        return itemRepository.findItemsByItemRequestId(requestId);
    }

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.getReferenceById(bookingId);
    }

    public boolean isItemExist(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    public boolean isItemAvailable(Long itemId) {
        return itemRepository.findById(itemId).get().getAvailable();
    }

    public boolean isBookingExistById(Long bookingId) {
        return bookingRepository.findById(bookingId).isPresent();
    }

    public boolean checkItemsToOwner(Long bookingId, Long userId) {
        Long bookerId = bookingRepository.findById(bookingId)
                .get().getBooker().getId();
        Long ownerId = itemRepository.findById(bookingRepository.findById(bookingId).get().getId())
                .get().getOwner().getId();
        if (!bookerId.equals(userId) && !ownerId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //нет доступа к просмотру бронирования
        }
        return false;
    }

    public List<Booking> findAllBookingsByBookerIdAndItemId(Long bookerId, Long itemId) {
        return bookingRepository.findAllBookingsByBookerIdAndItemId(bookerId, itemId);
    }

    public BookingOutDto getLastBooking(Long itemId) {
        return BookingMapper.toBookingOutDto(bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(itemId,
                LocalDateTime.now()));
    }

    public BookingOutDto getNextBooking(Long itemId) {
        return BookingMapper.toBookingOutDto(bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId,
                LocalDateTime.now()));
    }
}