package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id; //уникальный идентификатор вещи

    private String name; //краткое название

    private String description; //развёрнутое описание

    private Boolean available; //статус о том, доступна или нет вещь для аренды

    private User owner; //владелец вещи

    private ItemRequest itemRequest; //если вещь была создана по запросу другого пользователя, то храним его
}
