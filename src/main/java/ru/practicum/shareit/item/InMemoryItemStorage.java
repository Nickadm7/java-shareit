package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

import static java.util.stream.Collectors.toList;

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
        if (item.getName().isEmpty() || item.getDescription().isEmpty() || item.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (item.getDescription() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
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
    public List<Item> getAllItemsByOwner(Long ownerId) {
        return itemDbStorage.values().stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Item updateItem(Item item) {
        if (item.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Long currentId = item.getId();
        if (!itemDbStorage.containsKey(currentId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (item.getName() == null) {
            item.setName(itemDbStorage.get(currentId).getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemDbStorage.get(currentId).getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(itemDbStorage.get(currentId).getAvailable());
        }
        itemDbStorage.put(currentId, item);
        return itemDbStorage.get(currentId);
    }

    @Override
    public void deleteItemById(Long itemId) {
        if (itemDbStorage.containsKey(itemId)) {
            itemDbStorage.remove(itemId);
        } else {
            throw new IllegalArgumentException("user с id: " + itemId + " не найден!");
        }
    }

    @Override
    public Collection<Item> searchItem(String text) {
        List<Item> searchAvailableItems = new ArrayList<>();
        if (!text.isBlank()) {
            searchAvailableItems = itemDbStorage.values().stream()
                    .filter(Item::getAvailable)
                    .filter(item -> item.getName().toLowerCase().contains(text) ||
                            item.getDescription().toLowerCase().contains(text))
                    .collect(toList());
        }
        return searchAvailableItems;
    }

    public Long generateItemId() {
        return ++id;
    }
}
