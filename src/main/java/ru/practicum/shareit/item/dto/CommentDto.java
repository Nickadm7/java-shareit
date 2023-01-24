package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id; //уникальный идентификатор комментария

    @NotEmpty
    @NotBlank
    private String text; //текст комментария

    @JsonIgnore
    private Item item; //вещь, к которой написан комментарий

    private User author; //пользователь, который написал комментарий

    @Builder.Default
    private LocalDateTime created = LocalDateTime.now(); //время создания комментария
}