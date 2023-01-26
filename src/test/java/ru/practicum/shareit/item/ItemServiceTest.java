package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutForFindDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private Utils utils;
    @InjectMocks
    private ItemServiceImpl itemService;
    private final Item item = new Item(1L,
            "ItemTestName",
            "TestDescription",
            true,
            null,
            null);
    private final ItemDto itemDto = new ItemDto(1L,
            "ItemTestName",
            "TestDescription",
            true,
            null,
            null);

    @Test
    @DisplayName("Тест добавление новой вещи")
    void addItemTest() {
        when(itemRepository.save(item)).thenReturn(item);
        ItemDto itemDtoInput = new ItemDto(1L,
                "ItemTestName",
                "TestDescription",
                true,
                new User(),
                null);

        ItemDto actualItemDto = itemService.addItem(itemDtoInput, 1L);

        assertEquals(itemDto.getId(), actualItemDto.getId());
        assertEquals(itemDto.getDescription(), actualItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), actualItemDto.getAvailable());
        assertEquals(itemDto, actualItemDto);
    }

    @Test
    @DisplayName("Тест поиск вещи по тексту")
    void findItemsByTextTest() {
        when(itemRepository.findItemsByText(anyString())).thenReturn(List.of(item));

        List<ItemDto> actualItem = itemService.findItemsByText("text");

        verify(itemRepository, times(1)).findItemsByText(anyString());
    }

    @Test
    @DisplayName("Тест поиск вещи по id пользователя")
    void findItemsByUserIdTest() {
        when(itemRepository.findItemsByOwnerId(anyLong())).thenReturn(List.of(item));

        List<ItemOutForFindDto> actualItem = itemService.findItemsByUserId(1L);

        verify(itemRepository, times(1)).findItemsByOwnerId(anyLong());
    }

    @Test
    @DisplayName("Тест обновление вещи")
    void updateItemTest() {
        ItemDto newItemDto = new ItemDto(1L,
                "ItemTestName",
                "TestDescription",
                true,
                null,
                null);

        assertThrows(NoSuchElementException.class,
                () -> itemService.updateItem(newItemDto, 1L, 1L));
    }

    @Test
    @DisplayName("Тест обновление вещи")
    void updateItemWrongParameterTest() {
        ItemDto newItemDto = new ItemDto(1L,
                "ItemTestName",
                "TestDescription",
                true,
                null,
                null);

        assertThrows(NoSuchElementException.class,
                () -> itemService.updateItem(newItemDto, 0L, 0L));
    }

    @Test
    @DisplayName("Тест обновление вещи")
    void updateItemWrongParameterTest1() {
        ItemDto newItemDto = new ItemDto(1L,
                "ItemTestName",
                "",
                true,
                null,
                null);

        assertThrows(NoSuchElementException.class,
                () -> itemService.updateItem(newItemDto, 0L, 0L));
    }

    @Test
    @DisplayName("Тест удаление вещи по id")
    void deleteByIdTest() {
        assertThrows(NoSuchElementException.class,
                () -> itemService.deleteById(0L, 0L));
    }

    @Test
    @DisplayName("Тест конвертация CommentToOutDto")
    void converterCommentToOutDtoTest() {
        User author = new User(1L, "testName", "testEmail");
        Item item = new Item(1L,
                "name",
                "description",
                true,
                null,
                null);
        Comment comment = new Comment(2L, "text", item, author, LocalDateTime.now());
        List<CommentOutDto> actual = itemService.converterCommentToOutDto(List.of(comment));

        assertEquals(actual.get(0).getId(), 2L);
    }
}