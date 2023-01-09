package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final Utils utils;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        if (utils.isUserExist(ownerId)) {
            return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, utils.getUserById(ownerId))));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CommentOutDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        if (itemId == null || userId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //не найден id вещи или владельца
        }
        if (commentDto.getText().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //комментарий не может быть пустой
        }
        List<Booking> bufferBookings = utils.findAllBookingsByBookerIdAndItemId(userId, itemId);
        if (bufferBookings == null || bufferBookings.get(0).getStatus().equals(Status.REJECTED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //пользователь не арендовал вещь
        }
        boolean isRequetGood = false;
        for (Booking currentBooking : bufferBookings) {
            if (currentBooking.getStatus().equals(Status.APPROVED)
                    && currentBooking.getEnd().isBefore(LocalDateTime.now())) {
                isRequetGood = true;
                break;
            }
        }
        if (!isRequetGood) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //статус не APPROVED или вещь еще бронируется
        }

        Comment comment = new Comment();
        comment.setItem(utils.getItemById(itemId));
        comment.setAuthor(utils.getUserById(userId));
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return CommentMapper.toCommentOutDto(comment, userId);


    }

    @Override
    public ItemOutForFindDto getItemById(Long itemId, Long userId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //вещь не найдена
        }
        if (!utils.isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //пользователь не существует
        }
        ItemOutForFindDto itemOutForFindDto = new ItemOutForFindDto();
        Item item = utils.getItemById(itemId);
        List<Comment> currentComments = commentRepository.findAllByItemId(itemId);
        if (item.getOwner().getId().equals(userId)) {
            itemOutForFindDto = ItemMapper.toItemOutForOwnerDto(item
                    , utils.getLastBooking(itemId)
                    , utils.getNextBooking(itemId)
                    , converterCommentToOutDto(currentComments));
        } else {
            itemOutForFindDto = ItemMapper.toItemOutForOwnerDto(item
                    , null
                    , null
                    , converterCommentToOutDto(currentComments));
        }
        return itemOutForFindDto;
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        text = text.toLowerCase();
        return itemRepository.findItemsByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemOutForFindDto> findItemsByUserId(Long userId) {
        List<Item> items = itemRepository.findItemsByOwnerId(userId);
        List<ItemOutForFindDto> outItems = new ArrayList<>();
        items.forEach(item -> outItems.add(ItemMapper.toItemOutForOwnerDto(item
                , utils.getLastBooking(item.getId())
                , utils.getNextBooking(item.getId()),
                null)));
        return outItems.stream()
                .sorted(Comparator.comparing(ItemOutForFindDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        if (itemId == null || ownerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (itemDto.getName() == null) {
            itemDto.setName(itemRepository.findById(itemId).get().getName());
        }
        if (itemDto.getDescription() == null) {
            itemDto.setDescription(itemRepository.findById(itemId).get().getDescription());
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(itemRepository.findById(itemId).get().getAvailable());
        }
        if ((itemRepository.findById(itemId).get().getOwner().getId()).equals(ownerId)) {
            return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, utils.getUserById(ownerId))));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteById(Long itemId, Long ownerId) {
        if (itemRepository.findById(itemId).get().getOwner().getId().equals(ownerId)) {
            itemRepository.deleteById(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private List<CommentOutDto> converterCommentToOutDto(List<Comment> bufferOutComments) {
        return bufferOutComments.stream()
                .map(CommentMapper::toCommentOutForFindItemsDto)
                .collect(Collectors.toList());
    }
}
