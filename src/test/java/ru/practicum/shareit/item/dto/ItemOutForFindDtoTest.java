package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemOutForFindDtoTest {
    @Autowired
    JacksonTester<ItemOutForFindDto> json;

    @Test
    @DisplayName("Тест ItemOutForFindDto Json")
    void itemOutForFindDtoTest() throws Exception {
        ItemOutForFindDto itemOutForFindDto = new ItemOutForFindDto(
                1L,
                "testName",
                "testDescription",
                true,
                null,
                null,
                null
        );

        JsonContent<ItemOutForFindDto> result = json.write(itemOutForFindDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("testName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("testDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathStringValue("$.lastBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.nextBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.comments").isEqualTo(null);
    }
}
