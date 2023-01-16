package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final Utils utils;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long requestUserId, LocalDateTime created) {
        if (utils.isUserExist(requestUserId)) {
            ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto,
                    utils.getUserById(requestUserId),
                    created);
            ItemRequest outItemRequest = itemRequestRepository.save(itemRequest);
            return ItemRequestMapper.toItemRequestDto(outItemRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
    }

    @Override
    public List<ItemRequestDto> findAllItemRequestsByOwnerId(Long ownerId) {
        utils.isUserExist(ownerId);
        List<ItemRequest> outItemRequests = itemRequestRepository.findAllByRequestUserId_OrderByCreatedDesc(ownerId);
        return outItemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    public ItemRequestDto findItemRequestById(Long itemRequestId, Long userId) {
        if (utils.isUserExist(userId)) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                    .orElseThrow(() -> new IllegalArgumentException("Не найден запрос с id:" + itemRequestId));
            return ItemRequestMapper.toItemRequestDto(itemRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
    }
}
