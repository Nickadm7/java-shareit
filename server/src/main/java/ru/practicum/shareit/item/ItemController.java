package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
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
        return itemService.addItem(itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutDto addComments(@RequestHeader(OWNER_ID) Long userId,
                                     @PathVariable Long itemId,
                                     @RequestBody CommentDto commentDto) {
        log.info("POST-запрос к эндпоинту /{itemId}/comment:  владелец id:{} и вещь с id{}", userId, itemId);
        return itemService.addComment(commentDto, userId, itemId);
    }

    @GetMapping
    public List<ItemOutForFindDto> findItemsByUserId(@RequestHeader(OWNER_ID) Long userId) {
        log.info("GET-запрос к эндпоинту /items список всех вещей пользователя");
        return itemService.findItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemOutForFindDto getItemById(@PathVariable Long itemId, @RequestHeader(OWNER_ID) Long userId) {
        log.info("GET-запрос к эндпоинту /items найти пользователя по id: {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByText(@RequestParam String text) {
        log.info("GET-запрос к эндпоинту /items/search найти: {}", text);
        return itemService.findItemsByText(text);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                              @RequestHeader(OWNER_ID) Long ownerId) {
        log.info("PATCH-запрос к эндпоинту /items обновить пользователя по id: {}", itemId);
        utils.isUserExist(ownerId);
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable Long itemId, @RequestHeader(OWNER_ID) Long ownerId) {
        log.info("DELETE-запрос к эндпоинту /items удалить пользователя с id: {}", itemId);
        itemService.deleteById(itemId, ownerId);
    }
}