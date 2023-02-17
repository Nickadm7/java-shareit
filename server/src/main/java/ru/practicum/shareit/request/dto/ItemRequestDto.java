package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id; //уникальный идентификатор запроса
    @NotBlank
    private String description; //описание запроса

    //private User request; //пользователь, который создает запрос

    private LocalDateTime created; //когда был создан запрос
    private List<ItemDto> items;
}