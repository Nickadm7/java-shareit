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
        if (!utils.isUserExist(requestUserId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto,
                utils.getUserById(requestUserId),
                created);
        ItemRequest outItemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestOutDto(outItemRequest);
    }

    @Override
    public List<ItemRequestDto> findAllItemRequestsByOwnerId(Long ownerId) {
        utils.isUserExist(ownerId);
        List<ItemRequest> outItemRequests = itemRequestRepository.findAllByRequestUserId_OrderByCreatedDesc(ownerId);
        List<ItemRequestDto> requests = outItemRequests.stream()
                .map(ItemRequestMapper::toItemRequestOutDto)
                .collect(Collectors.toList());
        requests.forEach(itemRequestDto -> itemRequestDto.setItems(utils.findItemsByRequestId(itemRequestDto.getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList())));
        return requests;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        if (!utils.isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
        if (from < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //не корректный запрос
        }
        List<ItemRequestDto> requests = itemRequestRepository.findByRequestUserIdNot(userId,
                        PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created")))
                .stream()
                .map(ItemRequestMapper::toItemRequestOutDto)
                .collect(Collectors.toList());
        requests.forEach(itemRequestDto -> itemRequestDto.setItems(utils.findItemsByRequestId(itemRequestDto.getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList())));
        return requests;
    }

    @Override
    public ItemRequestDto findItemRequestById(Long itemRequestId, Long userId) {
        if (!utils.isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //пользователь не существует
        }
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


}
