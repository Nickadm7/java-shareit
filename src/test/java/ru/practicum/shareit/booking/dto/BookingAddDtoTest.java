package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingAddDtoTest {
    @Autowired
    JacksonTester<BookingAddDto> json;

    @Test
    @DisplayName("Тест BookingAddDto Json")
    void bookingAddDtoTest() throws Exception {
        LocalDateTime start = LocalDateTime.parse("2222-10-12T14:00");
        LocalDateTime end = LocalDateTime.parse("2222-12-12T14:00");
        BookingAddDto bookingAddDto = new BookingAddDto(
                1L,
                1L,
                start,
                end
        );

        JsonContent<BookingAddDto> result = json.write(bookingAddDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2222-10-12T14:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2222-12-12T14:00:00");
    }
}