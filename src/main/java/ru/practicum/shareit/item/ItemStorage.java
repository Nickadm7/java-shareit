package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

@Component
public interface ItemStorage {

    Item addItem(Item item);

    List<Item> getAllItemsByOwner(Long ownerId);

    Item getItemById(Long itemId);

    Collection<Item> searchItem(String text);

    void deleteItemById(Long itemId);

    Item updateItem(Item item);
}
