package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String OWNER_ID = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> itemRequestAdd(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                 @RequestHeader(OWNER_ID) Long requestUserId) {
        log.info("POST-запрос к эндпоинту //requests  requestUserId: {}", requestUserId);
        return itemRequestClient.create(requestUserId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsByOwnerId(@RequestHeader(OWNER_ID) Long ownerId) {
        log.info("GET-запрос к эндпоинту /items список всех вещей владельца");
        return itemRequestClient.findAllItemRequestsByOwnerId(ownerId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequestById(@PathVariable("requestId") Long itemRequestId,
                                                      @RequestHeader(OWNER_ID) Long userId) {
        log.info("GET-запрос к эндпоинту /items поиск запроса по id: {}", userId);
        return itemRequestClient.findItemRequestById(userId, itemRequestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(OWNER_ID) Long userId,
                                                     @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("GET-запрос к эндпоинту /items/all поиск всех запросов");
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }
}