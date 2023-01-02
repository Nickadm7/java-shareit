package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * TODO Sprint add-item-requests.
 */
//разработка в следующих спринтах
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //уникальный идентификатор запроса

    private String description; //описание запроса

    @ManyToOne
    @JoinColumn(name = "request_id")
    private User request; //
}