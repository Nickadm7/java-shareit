package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto);

    Collection<ItemDto> getAllItems();

    ItemDto getItemById(Long itemId);

    void deleteById(Long itemId);

    ItemDto updateItem(ItemDto itemDto, Long itemId);
}
