package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutForFindDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private Utils utils;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String OWNER_ID = "X-Sharer-User-Id";
    private final User user = new User(1L, "userTestName", "mailtest@mail.ru");
    private final Item item = new Item(1L,
            "name",
            "description",
            true,
            user,
            null);
    private final ItemDto itemDto = new ItemDto(1L,
            "ItemTestName",
            "TestDescription",
            true,
            null,
            null);
    private final ItemOutForFindDto itemOutForFindDto = new ItemOutForFindDto(1L,
            "ItemTestName",
            "TestDescription",
            true,
            null,
            null,
            null);
    private final CommentDto commentDto = new CommentDto(1L,
            "text",
            item,
            user,
            LocalDateTime.now());
    private final CommentOutDto commentOutDto = new CommentOutDto("text",
            "testName",
            LocalDateTime.now());


    @Test
    @DisplayName("Тест добавление вещи")
    void addItemsTest() throws Exception {
        when(itemService.addItem(any(), any(Long.class)))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(OWNER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    @DisplayName("Тест поиск вещи по id")
    void getItemByIdTest() throws Exception {
        when(itemService.getItemById(any(Long.class), any(Long.class)))
                .thenReturn(itemOutForFindDto);

        mockMvc.perform(get("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(OWNER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    @DisplayName("Тест удалить вещь по id")
    void deleteByIdTest() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .header(OWNER_ID, 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест поиск вещей по id пользователя")
    void findItemsByUserIdTest() throws Exception {
        when(itemService.findItemsByUserId(anyLong()))
                .thenReturn(List.of(itemOutForFindDto));

        mockMvc.perform(get("/items/")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(OWNER_ID, 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест поиск вещей по тексту")
    void findItemsByTest() throws Exception {
        when(itemService.findItemsByText("text"))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(OWNER_ID, 1L)
                        .queryParam("text", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест добавление нового комментария")
    void addCommentsTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(itemService.addComment(commentDto, 0L, 0L))
                .thenReturn(commentOutDto);

       mockMvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(OWNER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест обновление вещи")
    void updateItemTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(itemService.updateItem(itemDto, 0L, 0L))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(OWNER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}