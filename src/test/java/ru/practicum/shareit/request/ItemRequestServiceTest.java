package ru.practicum.shareit.request;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private Utils utils;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "testDescription",
            LocalDateTime.now(),
            List.of());
    ItemRequest itemRequest = new ItemRequest(
            1L,
            "testDescription",
            new User(),
            LocalDateTime.now());

    @Test
    @DisplayName("Тест добавление нового запроса")
    void addItemRequestTest() {
        when(itemRequestRepository.save(any(ItemRequest.class))).thenAnswer(returnsFirstArg());

        itemRequestService.addItemRequest(itemRequestDto, 1L, LocalDateTime.now());

        verify(itemRequestRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Тест поиск всех запросов по id владельца")
    void findAllItemRequestsByOwnerIdTest() {
        when(itemRequestRepository.findAllByRequestUserId_OrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(itemRequest));

        itemRequestService.findAllItemRequestsByOwnerId(1L);

        verify(itemRequestRepository, times(1)).findAllByRequestUserId_OrderByCreatedDesc(any());
    }

    @Test
    @DisplayName("Тест поиск запроса по id")
    void findItemRequestByIdTest() {
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));

        itemRequestService.findItemRequestById(1L, 1L);

        verify(itemRequestRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Тест поиск запроса по неверному id")
    void getAllItemRequestsWrongIdTest() {
        assertThrows(IllegalArgumentException.class,
                () -> itemRequestService.getAllItemRequests(99L, 0, 0));
    }

    @Test
    @DisplayName("Тест поиск всех запросов")
    void getAllItemRequestsTest() {
        itemRequestService.getAllItemRequests(1L, 1, 1);

        verify(itemRequestRepository, times(1)).findByRequestUserIdNot(anyLong(), any());
    }

    @Test
    @DisplayName("Тест поиск всех запросов from = 0")
    void getAllItemRequestsFromIsNullTest() {
        itemRequestService.getAllItemRequests(1L, null, 1);

        verify(itemRequestRepository, times(1)).findByRequestUserIdNot(anyLong());
    }
}