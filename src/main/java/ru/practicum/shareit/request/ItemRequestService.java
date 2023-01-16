package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest (ItemRequestDto itemRequestDto, Long requestUserId, LocalDateTime created);

    List<ItemRequestDto> findAllItemRequestsByOwnerId(Long ownerId);

    List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size);

    ItemRequestDto findItemRequestById(Long itemRequestId, Long userId);
}
