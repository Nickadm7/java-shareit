package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutForFindDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

class ItemServiceTest {
    private final ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
    private final BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
    private final Utils utils = Mockito.mock(Utils.class);

    private ItemServiceImpl itemService;
    private Item itemForTest;
    private ItemDto itemDtoForTest;
    private User userForTest;

    @BeforeEach
    void initUser() {
        userForTest = new User();
        userForTest.setId(1L);
        userForTest.setName("test");
        userForTest.setEmail("testmail@mail.ru");
    }

    @BeforeEach
    void initItem() {
        User user = new User(1L, "test", "testmail@mail.ru");
        itemForTest = new Item();
        itemForTest.setId(1L);
        itemForTest.setName("test");
        itemForTest.setDescription("testdescription");
        itemForTest.setAvailable(true);
        itemForTest.setItemRequest(null);
        itemForTest.setOwner(user);
    }

    @BeforeEach
    void initItemDto() {
        itemDtoForTest = new ItemDto();
        itemDtoForTest.setId(1L);
        itemDtoForTest.setName("test");
        itemDtoForTest.setDescription("testdescription");
        itemDtoForTest.setAvailable(true);
    }

    @BeforeEach
    void initItemService() {
        itemService = new ItemServiceImpl(mockItemRepository, mockCommentRepository, utils);
    }

    @Test
    @DisplayName("Тест создание вещи")
    void createItemTest() {
        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(itemForTest);
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));

        ItemDto itemDtoActual = itemService.addItem(itemDtoForTest, 1L);

        Assertions.assertEquals(itemDtoActual.getId(), itemForTest.getId());
        Assertions.assertEquals(itemDtoActual.getName(), itemForTest.getName());
        Assertions.assertEquals(itemDtoActual.getDescription(), itemForTest.getDescription());
        Assertions.assertEquals(itemDtoActual.getAvailable(), itemForTest.getAvailable());
    }

    @Test
    @DisplayName("Тест добавление комментария")
    void addCommentTest() {
        Booking bookingForTest1 = new Booking();
        bookingForTest1.setStatus(Status.APPROVED);
        bookingForTest1.setEnd(LocalDateTime.now().minusDays(3));
        List<Booking> bufferBookings = List.of(bookingForTest1);
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(userForTest));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));
        Mockito
                .when(utils.findAllBookingsByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(bufferBookings);
        Mockito
                .when(utils.getUserById(anyLong()))
                .thenReturn(userForTest);

        Comment commentForTest = new Comment();
        commentForTest.setId(1L);
        commentForTest.setText("test");
        commentForTest.setCreated(LocalDateTime.now());
        CommentDto commentForTestDto = new CommentDto(1L, "test", null, null, LocalDateTime.now());
        Mockito
                .when(mockCommentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(commentForTest);
        Booking bookingForTest = new Booking();
        bookingForTest.setStatus(Status.APPROVED);
        bookingForTest.setEnd(LocalDateTime.now().minusDays(3));
        Mockito
                .when(mockBookingRepository.findAllBookingsByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(List.of(bookingForTest));

        CommentOutDto commentDtoActual = itemService.addComment(commentForTestDto, 2L, 1L);

        Assertions.assertEquals(commentDtoActual.getText(), commentForTest.getText());
        Assertions.assertNotNull(commentDtoActual.getCreated());
        Assertions.assertEquals(commentDtoActual.getAuthorName(), userForTest.getName());
    }

    @Test
    @DisplayName("Тест добавление комментария когда статус Rejected")
    void addCommentFailStatusRejected() {
        Booking bookingForTest1 = new Booking();
        bookingForTest1.setStatus(Status.REJECTED);
        bookingForTest1.setEnd(LocalDateTime.now().minusDays(3));
        List<Booking> bufferBookings = List.of(bookingForTest1);
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(userForTest));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));
        Mockito
                .when(utils.findAllBookingsByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(bufferBookings);
        Mockito
                .when(utils.getUserById(anyLong()))
                .thenReturn(userForTest);
        Comment commentForTest = new Comment();
        commentForTest.setId(1L);
        commentForTest.setText("test");
        commentForTest.setCreated(LocalDateTime.now());
        CommentDto commentForTestDto = new CommentDto(1L, "test", null, null, LocalDateTime.now());
        Mockito
                .when(mockCommentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(commentForTest);
        Booking bookingForTest = new Booking();
        bookingForTest.setStatus(Status.APPROVED);
        bookingForTest.setEnd(LocalDateTime.now().minusDays(3));
        Mockito
                .when(mockBookingRepository.findAllBookingsByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(List.of(bookingForTest));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> itemService.addComment(commentForTestDto, 2L, 1L));

        Assertions.assertEquals(thrown.getMessage(), ("400 BAD_REQUEST"));
    }

    @Test
    @DisplayName("Тест добавление комментария с пустум текстом")
    void addCommentFailBlankCommentTest() {
        Booking bookingForTest1 = new Booking();
        bookingForTest1.setStatus(Status.APPROVED);
        bookingForTest1.setEnd(LocalDateTime.now().minusDays(3));
        List<Booking> bufferBookings = List.of(bookingForTest1);
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(userForTest));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));
        Mockito
                .when(utils.findAllBookingsByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(bufferBookings);
        Mockito
                .when(utils.getUserById(anyLong()))
                .thenReturn(userForTest);
        Comment commentForTest = new Comment();
        commentForTest.setId(1L);
        commentForTest.setText("test");
        commentForTest.setCreated(LocalDateTime.now());
        CommentDto commentForTestDto = new CommentDto(1L, "", null, null, LocalDateTime.now());
        Mockito
                .when(mockCommentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(commentForTest);
        Booking bookingForTest = new Booking();
        bookingForTest.setStatus(Status.APPROVED);
        bookingForTest.setEnd(LocalDateTime.now().minusDays(3));
        Mockito
                .when(mockBookingRepository.findAllBookingsByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(List.of(bookingForTest));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> itemService.addComment(commentForTestDto, 2L, 1L));

        Assertions.assertEquals(thrown.getMessage(), ("400 BAD_REQUEST"));
    }

    @Test
    @DisplayName("Тест добавление комментария статус не Approve")
    void addCommentFailStatusNotApprovedTest() {
        Booking bookingForTest1 = new Booking();
        bookingForTest1.setStatus(Status.WAITING);
        bookingForTest1.setEnd(LocalDateTime.now().minusDays(3));
        List<Booking> bufferBookings = List.of(bookingForTest1);
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(userForTest));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));
        Mockito
                .when(utils.findAllBookingsByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(bufferBookings);
        Mockito
                .when(utils.getUserById(anyLong()))
                .thenReturn(userForTest);
        Comment commentForTest = new Comment();
        commentForTest.setId(1L);
        commentForTest.setText("test");
        commentForTest.setCreated(LocalDateTime.now());
        CommentDto commentForTestDto = new CommentDto(1L, "test", null, null, LocalDateTime.now());
        Mockito
                .when(mockCommentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(commentForTest);
        Booking bookingForTest = new Booking();
        bookingForTest.setStatus(Status.APPROVED);
        bookingForTest.setEnd(LocalDateTime.now().minusDays(3));
        Mockito
                .when(mockBookingRepository.findAllBookingsByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(List.of(bookingForTest));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> itemService.addComment(commentForTestDto, 2L, 1L));

        Assertions.assertEquals(thrown.getMessage(), ("400 BAD_REQUEST"));
    }

    @Test
    @DisplayName("Тест обновление вещи не владельцем")
    void updateItemFailNotOwner() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));
        itemDtoForTest.setName("Отвертка обычная");
        itemDtoForTest.setDescription("Крестовая отвертка");
        itemDtoForTest.setAvailable(Boolean.FALSE);
        Mockito
                .when(mockItemRepository.findItemsByOwnerId(anyLong()))
                .thenReturn(List.of(itemForTest));
        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(itemForTest);
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class,
                () -> itemService.updateItem(itemDtoForTest, 1L, 0L));

        Assertions.assertEquals("404 NOT_FOUND", thrown.getMessage());
    }

    @Test
    @DisplayName("Тест обновление вещи поля null")
    void updateItemNullField() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));
        Mockito
                .when(mockItemRepository.findItemsByOwnerId(anyLong()))
                .thenReturn(List.of(itemForTest));
        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(itemForTest);
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        itemDtoForTest.setId(null);
        itemDtoForTest.setName(null);
        itemDtoForTest.setDescription(null);
        itemDtoForTest.setAvailable(null);

        itemService.updateItem(itemDtoForTest, 1L, 1L);
    }

    @Test
    @DisplayName("Тест поиск вещи по тексту")
    void findItemsByTextTest() {
        Mockito
                .when(mockItemRepository.findItemsByText(anyString()))
                .thenReturn(List.of(itemForTest));

        List<ItemDto> actualItem = itemService.findItemsByText("text");

        Mockito.verify(mockItemRepository, times(1)).findItemsByText(anyString());
    }

    @Test
    @DisplayName("Тест поиск вещи по пустому тексту")
    void findItemsByNullTextTest() {
        Mockito
                .when(mockItemRepository.findItemsByText(anyString()))
                .thenReturn(List.of(itemForTest));

        List<ItemDto> actualItem = itemService.findItemsByText("");

        Mockito.verify(mockItemRepository, times(0)).findItemsByText(anyString());
    }

    @Test
    @DisplayName("Тест поиск вещи по id пользователя")
    void findItemsByUserIdTest() {
        Mockito.when(mockItemRepository.findItemsByOwnerId(anyLong())).thenReturn(List.of(itemForTest));

        List<ItemOutForFindDto> actualItem = itemService.findItemsByUserId(1L);

        Mockito.verify(mockItemRepository, times(1)).findItemsByOwnerId(anyLong());
    }

    @Test
    @DisplayName("Тест удаление вещи по неправильному id")
    void deleteByWrongIdTest() {
        assertThrows(NoSuchElementException.class,
                () -> itemService.deleteById(99L, 1L));
    }

    @Test
    @DisplayName("Тест удаление вещи по id")
    void deleteByIdTest() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));
        itemService.deleteById(1L, 1L);
    }

    @Test
    @DisplayName("Тест удаление вещи не владельцем")
    void deleteByIdWrongOwnerTest() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class,
                () -> itemService.deleteById(1L, 99L));

        Assertions.assertEquals(thrown.getMessage(), ("400 BAD_REQUEST"));
    }

    @Test
    @DisplayName("Тест поиск вещи по неправильному id")
    void getItemByIdWrongIdTest() {
        ResponseStatusException thrown =
                assertThrows(ResponseStatusException.class, () -> itemService.getItemById(99L, 99L));

        Assertions.assertEquals("404 NOT_FOUND", thrown.getMessage());
    }

    @Test
    @DisplayName("Тест поиск вещи по id владельцем")
    void getItemByIdOwnerTest() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));
        Mockito
                .when(utils.getItemById(anyLong()))
                .thenReturn((itemForTest));

        ItemOutForFindDto actualItem = itemService.getItemById(1L, 1L);

        Assertions.assertEquals(1L, actualItem.getId());
    }

    @Test
    @DisplayName("Тест поиск вещи по id не владельцем")
    void getItemByIdNotOwnerTest() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemForTest));
        Mockito
                .when(utils.getItemById(anyLong()))
                .thenReturn((itemForTest));

        ItemOutForFindDto actualItem = itemService.getItemById(1L, 99L);

        Assertions.assertEquals(1L, actualItem.getId());
    }
}