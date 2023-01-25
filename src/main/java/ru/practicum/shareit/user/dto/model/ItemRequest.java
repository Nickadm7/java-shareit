package ru.practicum.shareit.user.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_request")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //уникальный идентификатор запроса

    private String description; //описание запроса

    @ManyToOne
    @JoinColumn(name = "requestuser_id", referencedColumnName = "id")
    private User requestUser; //пользователь, который создает запрос

    private LocalDateTime created; //когда был создан запрос
}