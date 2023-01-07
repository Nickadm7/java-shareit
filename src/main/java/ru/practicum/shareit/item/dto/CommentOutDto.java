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
public class CommentOutDto {
    private Long id; //id пользователя оставившего комментарий
    @NotEmpty
    @NotBlank
    private String text; //текст комментария

    private String authorName; //пользователь, который написал комментарий

    private LocalDateTime created; //время создания комментария

    public CommentOutDto(String text, String authorName, LocalDateTime created) {
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }
}


