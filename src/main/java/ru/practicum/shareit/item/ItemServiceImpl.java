package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemStorage itemStorage;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, ItemStorage itemStorage) {
        this.itemMapper = itemMapper;
        this.itemStorage = itemStorage;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        return itemMapper.toItemDto(itemStorage.addItem(itemMapper.toItem(itemDto, ownerId)));

    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemMapper.toItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        text = text.toLowerCase();
        return itemStorage.searchItem(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {
        return itemStorage.getAllItemsByOwner(ownerId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        if (itemStorage.getItemById(itemId).getOwner().equals(ownerId)) {
            return itemMapper.toItemDto(itemStorage.updateItem(itemMapper.toItem(itemDto, ownerId)));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteById(Long itemId, Long ownerId) {
        if (itemStorage.getItemById(itemId).getOwner().equals(ownerId)) {
            itemStorage.deleteItemById(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
