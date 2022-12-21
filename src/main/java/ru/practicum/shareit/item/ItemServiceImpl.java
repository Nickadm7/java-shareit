package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final Utils utils;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        if (utils.isUserExist(ownerId)) {
            return ItemMapper.toItemDto(itemStorage.addItem(ItemMapper.toItem(itemDto, utils.getUserById(ownerId))));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        text = text.toLowerCase();
        return itemStorage.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {
        return itemStorage.getAllItemsByOwner(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        if ((itemStorage.getItemById(itemId).getOwner().getId()).equals(ownerId)) {
            return ItemMapper.toItemDto(itemStorage.updateItem(ItemMapper.toItem(itemDto, utils.getUserById(ownerId))));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteById(Long itemId, Long ownerId) {
        if (itemStorage.getItemById(itemId).getOwner().getId().equals(ownerId)) {
            itemStorage.deleteItemById(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
