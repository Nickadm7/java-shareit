package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

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

    public boolean isItemExist(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    public boolean isItemAvailable(Long itemId) {
        return itemRepository.findById(itemId).get().getAvailable(); //TODO
    }

    public boolean isBookingExistById(Long bookingId) {
        return bookingRepository.findById(bookingId).isPresent();
    }
}