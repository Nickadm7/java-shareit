package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Long ownerId);

    List<ItemOutForFindDto> findItemsByUserId(Long userId);

    ItemOutForFindDto getItemById(Long itemId, Long userId);

    List<ItemDto> findItemsByText(String text);

    void deleteById(Long itemId, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    CommentOutDto addComment(CommentDto commentDto, Long ownerId, Long itemId);
}