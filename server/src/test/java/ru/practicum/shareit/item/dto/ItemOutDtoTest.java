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
public class ItemOutDtoTest {
    @Autowired
    JacksonTester<ItemOutDto> json;

    @Test
    @DisplayName("Тест ItemOutDto Json")
    void itemOutDtoTest() throws Exception {
        User owner = new User(1L, "userName", "userDescription");
        ItemOutDto itemOutDto = new ItemOutDto(
                1L,
                "testName",
                "testDescription",
                true,
                owner,
                null
        );

        JsonContent<ItemOutDto> result = json.write(itemOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("testName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("testDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.owner.email").isEqualTo("userDescription");
    }
}