//package ru.practicum.shareit.booking;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.booking.dto.BookingAddDto;
//import ru.practicum.shareit.booking.dto.BookingDto;
//import ru.practicum.shareit.booking.model.Status;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.model.User;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = BookingController.class)
//public class BookingControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private BookingService bookingService;
//    @MockBean
//    private BookingRepository bookingRepository;
//    private final ObjectMapper mapper = new ObjectMapper();
//    private static final String OWNER_ID = "X-Sharer-User-Id";
//    LocalDateTime start = LocalDateTime.parse("2222-10-12T14:00");
//    LocalDateTime wrongStart = LocalDateTime.parse("1111-10-12T14:00");
//    LocalDateTime end = LocalDateTime.parse("2222-12-12T14:00");
//    User owner = new User(1L, "userOwnerTest", "userOwnerTest@mail.ru");
//    User booker = new User(2L, "userBookerTest", "userBookerTest@mail.ru");
//    Item item = new Item(1L,
//            "item",
//            "description",
//            true,
//            owner,
//            null);
//    BookingAddDto bookingAddDto = new BookingAddDto(1L,
//            1L,
//            start,
//            end);
//    BookingAddDto bookingAddDtoWrongStart = new BookingAddDto(1L,
//            1L,
//            wrongStart,
//            end);
//    BookingDto bookingDto = new BookingDto(1L,
//            start,
//            end,
//            item,
//            booker,
//            Status.APPROVED
//    );
//
//    @Test
//    @DisplayName("Тест добавление бронирования")
//    void addBookingTest() throws Exception {
//        mapper.registerModule(new JavaTimeModule());
//
//        when(bookingService.addBooking(any(), any(Long.class)))
//                .thenReturn(bookingDto);
//
//        mockMvc.perform(post("/bookings")
//                        .content(mapper.writeValueAsString(bookingAddDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(OWNER_ID, 1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
//                .andExpect(jsonPath("$.start",
//                        is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
//                .andExpect(jsonPath("$.end",
//                        is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
//                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
//    }
//
//    @Test
//    @DisplayName("Тест добавление бронирования неверная дата начала")
//    void addBookingWrongStartTest() throws Exception {
//        mapper.registerModule(new JavaTimeModule());
//
//        when(bookingService.addBooking(any(), any(Long.class)))
//                .thenReturn(bookingDto);
//
//        mockMvc.perform(post("/bookings")
//                        .content(mapper.writeValueAsString(bookingAddDtoWrongStart))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(OWNER_ID, 1))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    @DisplayName("Тест получение бронирования по id")
//    void getBookingByIdTest() throws Exception {
//        mapper.registerModule(new JavaTimeModule());
//
//        when(bookingService.getBookingById(any(Long.class), any(Long.class)))
//                .thenReturn(bookingDto);
//
//        mockMvc.perform(get("/bookings/1")
//                        .content(mapper.writeValueAsString(bookingAddDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(OWNER_ID, 1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
//                .andExpect(jsonPath("$.start",
//                        is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
//                .andExpect(jsonPath("$.end",
//                        is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
//                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), Status.class));
//    }
//
//    @Test
//    @DisplayName("Тест получение всех бронирований пользователя")
//    void getAllBookingsByUserTest() throws Exception {
//        mapper.registerModule(new JavaTimeModule());
//
//        when(bookingService.getAllBookingsByUser(any(Long.class), any(), any(), any()))
//                .thenReturn(List.of(bookingDto));
//
//        mockMvc.perform(get("/bookings")
//                        .content(mapper.writeValueAsString(bookingAddDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(OWNER_ID, 1))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Тест получение всех бронирований владельца")
//    void getAllBookingsByOwnerTest() throws Exception {
//        mapper.registerModule(new JavaTimeModule());
//
//        when(bookingService.getAllBookingsByOwner(anyLong(), any(), anyInt(), anyInt()))
//                .thenReturn(List.of(bookingDto));
//
//        mockMvc.perform(get("/bookings/owner")
//                        .content(mapper.writeValueAsString(bookingAddDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(OWNER_ID, 1))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Тест получение всех бронирований владельца")
//    void updateBookingByOwnerTest() throws Exception {
//        mapper.registerModule(new JavaTimeModule());
//
//        when(bookingService.updateBookingByOwner(anyLong(), anyLong(), any()))
//                .thenReturn(bookingDto);
//
//        mockMvc.perform(patch("/bookings/1")
//                        .content(mapper.writeValueAsString(bookingAddDto))
//                        .param("approved", String.valueOf(true))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(OWNER_ID, 1))
//                .andExpect(status().isOk());
//    }
//}