package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Long ownerId);

    List<ItemDto> getAllItemsByOwner(Long ownerId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> searchItem(String text);

    void deleteById(Long itemId, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);
}