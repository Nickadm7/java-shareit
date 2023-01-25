package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        if (item.getItemRequest() != null) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getItemRequest().getId()
        );
        } else {
            return new ItemDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getOwner(),
                    null
            );
        }
    }

    public static Item toItem(ItemDto itemDto, User owner, ItemRequest itemRequest) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                itemRequest
        );
    }

    public static ItemOutForFindDto toItemOutForOwnerDto(Item item,
                                                         BookingOutDto lastBooking,
                                                         BookingOutDto nextBooking,
                                                         List<CommentOutDto> currentComments) {
        return new ItemOutForFindDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                currentComments
        );
    }
}