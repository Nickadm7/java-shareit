package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final Utils utils;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        if (utils.isUserExist(ownerId)) {
            return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, utils.getUserById(ownerId))));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long ownerId, Long itemId) {
        if (itemId == null || ownerId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (commentDto.getText().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //комментарий не может быть пустой
        }
        return null;
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ItemMapper.toItemDto(itemRepository.getReferenceById(itemId));
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        text = text.toLowerCase();
        return itemRepository.findItemsByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemsByOwnerId(Long ownerId) {
        return itemRepository.findItemsByOwnerId(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        if (itemId == null || ownerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (itemDto.getName() == null) {
            itemDto.setName(itemRepository.findById(itemId).get().getName());
        }
        if (itemDto.getDescription() == null) {
            itemDto.setDescription(itemRepository.findById(itemId).get().getDescription());
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(itemRepository.findById(itemId).get().getAvailable());
        }
        if ((itemRepository.findById(itemId).get().getOwner().getId()).equals(ownerId)) {
            return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, utils.getUserById(ownerId))));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteById(Long itemId, Long ownerId) {
        if (itemRepository.findById(itemId).get().getOwner().getId().equals(ownerId)) {
            itemRepository.deleteById(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
