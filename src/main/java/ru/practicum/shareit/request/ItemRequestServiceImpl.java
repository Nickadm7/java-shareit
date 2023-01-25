package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.model.ItemRequest;
import ru.practicum.shareit.utils.Utils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final Utils utils;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long requestUserId, LocalDateTime created) {
        utils.isUserExist(requestUserId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto,
                utils.getUserById(requestUserId),
                created);
        ItemRequest outItemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestOutDto(outItemRequest);
    }

    @Override
    public List<ItemRequestDto> findAllItemRequestsByOwnerId(Long ownerId) {
        utils.isUserExist(ownerId);
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestUserId_OrderByCreatedDesc(ownerId);
        List<ItemRequestDto> outRequests = itemRequestToItemRequestDto(requests);
        return itemRequestDtoSetItem(outRequests);
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        utils.isUserExist(userId);
        if (size == null || from == null) { //параметры не определены, выводим все
            List<ItemRequest> requestsAll = itemRequestRepository.findByRequestUserIdNot(userId);
            List<ItemRequestDto> outRequestsAll = itemRequestToItemRequestDto(requestsAll);
            if (outRequestsAll.isEmpty()) {
                return Collections.emptyList();
            }
            return itemRequestDtoSetItem(outRequestsAll);
        }
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        List<ItemRequest> requests = itemRequestRepository.findByRequestUserIdNot(userId, pageRequest);
        List<ItemRequestDto> outRequests = itemRequestToItemRequestDto(requests);
        return itemRequestDtoSetItem(outRequests);
    }

    @Override
    public ItemRequestDto findItemRequestById(Long itemRequestId, Long userId) {
        utils.isUserExist(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Item> bufferItems = utils.findItemsByRequestId(itemRequestId);
        return ItemRequestMapper.toItemRequestDto(itemRequest, converterItemToItemDto(bufferItems));
    }

    private List<ItemDto> converterItemToItemDto(List<Item> bufferItems) {
        return bufferItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private List<ItemRequestDto> itemRequestDtoSetItem(List<ItemRequestDto> bufferItems) {
        bufferItems.forEach(itemRequestDto -> itemRequestDto.setItems(utils.findItemsByRequestId(itemRequestDto.getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList())));
        return bufferItems;
    }

    private List<ItemRequestDto> itemRequestToItemRequestDto(List<ItemRequest> bufferItems) {
        return bufferItems
                .stream()
                .map(ItemRequestMapper::toItemRequestOutDto)
                .collect(Collectors.toList());
    }
}