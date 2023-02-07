package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id; //уникальный идентификатор комментария

    @NotBlank
    private String text; //текст комментария

    private String authorName; //пользователь, который написал комментарий
}