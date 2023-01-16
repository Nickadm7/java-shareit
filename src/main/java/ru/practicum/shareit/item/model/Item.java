package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //уникальный идентификатор вещи

    private String name; //краткое название

    private String description; //развёрнутое описание

    private Boolean available; //статус о том, доступна или нет вещь для аренды

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; //владелец вещи

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest itemRequest; //если вещь была создана по запросу другого пользователя, то храним его
}