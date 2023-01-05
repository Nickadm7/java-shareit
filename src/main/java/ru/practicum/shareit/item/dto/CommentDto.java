package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id; //уникальный идентификатор комментария
    private String text; //текст комментария
    private Item item; //вещь, к которой написан комментарий
    private User author; //пользователь, который написал комментарий
    private LocalDateTime created;
}
