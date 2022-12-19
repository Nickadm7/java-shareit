package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Component
public interface ItemStorage {

    Item addItem(Item item);

    Collection<Item> getAllItems();

    Item getItemById(Long itemId);

    Collection<Item> searchItem(String text);

    void deleteItemById(Long itemId);

    Item updateItem(Item item);
}
