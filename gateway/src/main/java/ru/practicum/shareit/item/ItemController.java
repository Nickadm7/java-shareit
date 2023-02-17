package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;
    //private final Utils utils;
    private static final String OWNER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItems(@Valid @RequestBody ItemDto itemDto,
                                           @RequestHeader(OWNER_ID) Long ownerId) {
        log.info("POST-запрос к эндпоинту /items owner_id: {}", ownerId);
        return itemClient.create(ownerId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComments(@RequestHeader(OWNER_ID) long userId,
                                              @PathVariable Long itemId,
                                              @Valid @RequestBody CommentDto commentDto) {
        log.info("POST-запрос к эндпоинту /{itemId}/comment:  владелец id:{} и вещь с id{}", userId, itemId);
        return itemClient.createComment(userId, itemId, commentDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId,
                                              @RequestHeader(OWNER_ID) Long userId) {
        log.info("GET-запрос к эндпоинту /items найти пользователя по id: {}", itemId);
        return itemClient.findById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findItemsByUserId(@RequestHeader(OWNER_ID) Long userId,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "100") @Positive int size) {
        log.info("GET-запрос к эндпоинту /items список всех вещей пользователя");
        return itemClient.findItemsByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemsByText(@RequestParam String text,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "100") @Positive int size) {
        log.info("GET-запрос к эндпоинту /items/search найти: {}", text);
        if (text.isBlank()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        } else {
            return itemClient.getItemByText(text, from, size);
        }
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @PathVariable Long itemId,
                                             @RequestHeader(OWNER_ID) Long ownerId) {
        log.info("PATCH-запрос к эндпоинту /items обновить пользователя по id: {}", itemId);
        //utils.isUserExist(ownerId);
        return itemClient.update(itemDto, itemId, ownerId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable Long itemId,
                           @RequestHeader(OWNER_ID) Long ownerId) {
        log.info("DELETE-запрос к эндпоинту /items удалить пользователя с id: {}", itemId);
        itemClient.delete(itemId, ownerId);
    }
}