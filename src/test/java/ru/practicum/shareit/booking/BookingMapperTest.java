package ru.practicum.shareit.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingMapperTest {

    @Test
    @DisplayName("Тест маппера toBookingOutDto")
    void toBookingOutDtoTest() {
        LocalDateTime start = LocalDateTime.parse("2222-10-12T14:00");
        LocalDateTime end = LocalDateTime.parse("2222-12-12T14:00");
        User user = new User(1L, "testName", "testEmail");
        Item item = new Item(2L,
                "ItemTestName",
                "TestDescription",
                true,
                null,
                null);
        Booking booking = new Booking(
                3L,
                start,
                end,
                item,
                user,
                Status.APPROVED
        );

        BookingOutDto actual = BookingMapper.toBookingOutDto(booking);

        assertEquals(3, actual.getId());
        assertEquals(1, actual.getBookerId());
        assert (actual.getStartTime() != null);
        assert (actual.getEndTime() != null);
    }
}