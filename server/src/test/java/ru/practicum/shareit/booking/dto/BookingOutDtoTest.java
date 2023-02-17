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
public class BookingOutDtoTest {
    @Autowired
    JacksonTester<BookingOutDto> json;

    @Test
    @DisplayName("Тест BookingOutDto Json")
    void bookingOutDtoTest() throws Exception {
        LocalDateTime start = LocalDateTime.parse("2222-10-12T14:00");
        LocalDateTime end = LocalDateTime.parse("2222-12-12T14:00");
        BookingOutDto bookingOutDto = new BookingOutDto(
                1L,
                1L,
                start,
                end
        );

        JsonContent<BookingOutDto> result = json.write(bookingOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.startTime").isEqualTo("2222-10-12T14:00:00");
        assertThat(result).extractingJsonPathStringValue("$.endTime").isEqualTo("2222-12-12T14:00:00");
    }
}
