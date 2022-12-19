package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.Utils;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final Utils utils;
    private static final String OWNER_ID = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService, Utils utils) {
        this.itemService = itemService;
        this.utils = utils;
    }

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
    public Collection<ItemDto> getAllItems() {
        log.info("GET-запрос к эндпоинту /items список всех пользователей");
        return itemService.getAllItems();
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
    public void deleteById(@PathVariable Long itemId) {
        log.info("DELETE-запрос к эндпоинту /items удалить пользователя с id: {}", itemId);
        itemService.deleteById(itemId);
    }
}
