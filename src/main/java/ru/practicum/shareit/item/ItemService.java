package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutForFindDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Long ownerId);

    List<ItemDto> findItemsByOwnerId(Long ownerId);

    ItemOutForFindDto getItemById(Long itemId, Long userId);

    List<ItemDto> findItemsByText(String text);

    void deleteById(Long itemId, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    CommentOutDto addComment(CommentDto commentDto, Long ownerId, Long itemId);
}