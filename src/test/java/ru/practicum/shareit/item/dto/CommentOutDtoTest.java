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
public class CommentOutDtoTest {
    @Autowired
    JacksonTester<CommentOutDto> json;

    @Test
    @DisplayName("Тест CommentOutDto Json")
    void commentOutDtoTest() throws Exception {
        LocalDateTime created = LocalDateTime.parse("2222-10-12T14:00");
        CommentOutDto commentOutDto = new CommentOutDto(
                1L,
                "testName",
                "testAuthorName",
                created
        );
        CommentOutDto commentOutDtoConstructor = new CommentOutDto(
                "testName",
                "testAuthorName",
                created
        );

        JsonContent<CommentOutDto> result = json.write(commentOutDto);
        JsonContent<CommentOutDto> resultConstructor = json.write(commentOutDtoConstructor);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("testName");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("testAuthorName");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2222-10-12T14:00:00");
        assertThat(resultConstructor).extractingJsonPathStringValue("$.text").isEqualTo("testName");
        assertThat(resultConstructor).extractingJsonPathStringValue("$.authorName").isEqualTo("testAuthorName");
        assertThat(resultConstructor).extractingJsonPathStringValue("$.created").isEqualTo("2222-10-12T14:00:00");
    }
}
