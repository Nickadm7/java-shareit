package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoTest {
    @Autowired
    JacksonTester<CommentDto> json;

    @Test
    @DisplayName("Тест ItemDto Json")
    void itemDtoTest() throws Exception {
        LocalDateTime created = LocalDateTime.parse("2222-10-12T14:00");
        CommentDto commentDto = new CommentDto(
                1L,
                "testName",
                null,
                null,
                created
        );

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("testName");
        assertThat(result).extractingJsonPathStringValue("$.item").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.owner").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2222-10-12T14:00:00");
    }
}
