package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Utils {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    public boolean isUserExist(Long ownerId) {
        return userService.getUserById(ownerId) != null;
    }

    public User getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }

    public Item getItemById(Long itemId) {
        return itemRepository.getReferenceById(itemId);
    }

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.getReferenceById(bookingId);
    }

    public boolean isItemExist(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    public boolean isItemAvailable(Long itemId) {
        return itemRepository.findById(itemId).get().getAvailable(); //TODO
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
}