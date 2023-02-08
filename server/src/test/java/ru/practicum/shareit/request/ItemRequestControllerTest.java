package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;
    @MockBean
    private Utils utils;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String OWNER_ID = "X-Sharer-User-Id";
    ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "testDescription",
            LocalDateTime.now(),
            List.of());

    @Test
    @DisplayName("Тест добавления нового ItemRequest")
    void addItemRequestTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(itemRequestService.addItemRequest(any(), anyLong()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(OWNER_ID, 1))
                .andExpect(status().isOk());
        verify(itemRequestService, times(1))
                .addItemRequest(any(ItemRequestDto.class), any());
    }

    @Test
    @DisplayName("Тест получение всех запросов по id владельца")
    void getAllItemRequestsByOwnerIdTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(itemRequestService.getAllItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(OWNER_ID, 1))
                .andExpect(status().isOk());
        verify(itemRequestService, times(1))
                .findAllItemRequestsByOwnerId(anyLong());
    }

    @Test
    @DisplayName("Тест получение запроса по id")
    void findItemRequestByIdTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(itemRequestService.findItemRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

       mockMvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(OWNER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест получение всех запросов")
    void getAllItemRequestsTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(itemRequestService.getAllItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(OWNER_ID, 1))
                .andExpect(status().isOk());
    }
}