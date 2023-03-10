package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id; //уникальный идентификатор пользователя

    private String name; //имя или логин пользователя

    @Email
    private String email; //адрес электронной почты
}