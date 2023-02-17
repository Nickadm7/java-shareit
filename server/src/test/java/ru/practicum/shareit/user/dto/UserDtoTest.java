package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {
    @Autowired
    JacksonTester<UserDto> json;

    @Test
    @DisplayName("Тест UserDto Json")
    void userDtoTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("testName");
        userDto.setEmail("testEmail");

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("testName");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("testEmail");
    }
}