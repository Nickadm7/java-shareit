package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.Utils;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final Utils utils;
    private static final String OWNER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItems(@Valid @RequestBody ItemDto itemDto, @RequestHeader(OWNER_ID) Long ownerId) {
        log.info("POST-запрос к эндпоинту /items owner_id: {}", ownerId);
        if (utils.isUserExist(ownerId)) {
            return itemService.addItem(itemDto, ownerId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader(OWNER_ID) Long ownerId) {
        log.info("GET-запрос к эндпоинту /items список всех вещей пользователя");
        return itemService.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("GET-запрос к эндпоинту /items найти пользователя по id: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("GET-запрос к эндпоинту /items/search найти: {}", text);
        return itemService.searchItem(text);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                              @RequestHeader(OWNER_ID) Long ownerId) {
        log.info("PATCH-запрос к эндпоинту /items обновить пользователя по id: {}", itemId);
        if (utils.isUserExist(ownerId)) {
            return itemService.updateItem(itemDto, itemId, ownerId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable Long itemId, @RequestHeader(OWNER_ID) Long ownerId) {
        log.info("DELETE-запрос к эндпоинту /items удалить пользователя с id: {}", itemId);
        itemService.deleteById(itemId, ownerId);
    }
}