package ru.practicum.shareit.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    JacksonTester<ItemRequestDto> json;
    @Test
    @DisplayName("Тест ItemRequestDto Json")
    void itemRequestDtoTest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1L,
                "testDescription",
                LocalDateTime.parse("2222-10-12T14:00"),
                new ArrayList<>()
        );

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("testDescription");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2222-10-12T14:00:00");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(Collections.emptyList());
    }
}
