package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String OWNER_ID = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto itemRequestAdd(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(OWNER_ID) Long requestUserId) {
        log.info("POST-запрос к эндпоинту //requests  requestUserId: {}", requestUserId);
        return itemRequestService.addItemRequest(itemRequestDto, requestUserId, LocalDateTime.now());
    }

    @GetMapping
    public List<ItemRequestDto> getAllItemRequestsByOwnerId(@RequestHeader(OWNER_ID) Long ownerId) {
        return itemRequestService.findAllItemRequestsByOwnerId(ownerId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findItemRequestById(@PathVariable("requestId") Long itemRequestId,
                                              @RequestHeader(OWNER_ID) Long userId) {
        return itemRequestService.findItemRequestById(itemRequestId, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(OWNER_ID) Long userId,
                                                   @RequestParam(name = "from", required = false) Integer from,
                                                   @RequestParam(name = "size", required = false) Integer size) {
        return itemRequestService.getAllItemRequests(userId, from, size);
    }
}