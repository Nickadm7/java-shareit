package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTest {
    @Autowired
    JacksonTester<ItemDto> json;

    @Test
    @DisplayName("Тест ItemDto Json")
    void itemDtoTest() throws Exception {
        User owner = new User(1L, "userName", "userDescription");
        ItemDto itemDto = new ItemDto(
                1L,
                "testName",
                "testDescription",
                true,
                owner,
                1L
        );

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("testName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("testDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.owner.email").isEqualTo("userDescription");
    }
}