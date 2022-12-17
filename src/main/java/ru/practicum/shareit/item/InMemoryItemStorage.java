package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> itemDbStorage;

    Long id;

    public InMemoryItemStorage() {
        itemDbStorage = new HashMap<>();
        id = 0L;
    }

    @Override
    public Item addItem(Item item) {

            if (item.getId() == null) {
                item.setId(generateItemId());
            }
            itemDbStorage.put(item.getId(), item);
            return itemDbStorage.get(item.getId());

    }

    @Override
    public Item getItemById(Long itemId) {
        if (itemDbStorage.containsKey(itemId)) {
            return itemDbStorage.get(itemId);
        } else {
            throw new IllegalArgumentException("user с id: " + itemId + " не найден!");
        }
    }

    @Override
    public Collection<Item> getAllItems() {
        return itemDbStorage.values();
    }

    @Override
    public Item updateItem(Item item) {
        itemDbStorage.put(item.getId(), item);
        return itemDbStorage.get(item.getId());
    }

    @Override
    public void deleteItemById(Long itemId) {
        if (itemDbStorage.containsKey(itemId)) {
            itemDbStorage.remove(itemId);
        } else {
            throw new IllegalArgumentException("user с id: " + itemId + " не найден!");
        }
    }

    public Long generateItemId() {
        return ++id;
    }
}
