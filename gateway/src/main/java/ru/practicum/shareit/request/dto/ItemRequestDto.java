package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id; //уникальный идентификатор запроса
    @NotBlank
    private String description; //описание запроса

    private Long requestorId; //пользователь, который создает запрос

    private LocalDateTime created; //когда был создан запрос
    //private List<ItemDto> items;
}