package ru.practicum.shareit.item.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //уникальный идентификатор комментария
    @NotBlank
    @NotEmpty
    private String text; //текст комментария
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item; //вещь, к которой написан комментарий
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author; //пользователь, который написал комментарий
    private LocalDateTime created;
}
