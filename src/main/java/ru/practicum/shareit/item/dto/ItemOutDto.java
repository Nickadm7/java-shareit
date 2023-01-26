package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.dto.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemOutDto {
    private Long id; //уникальный идентификатор вещи

    private String name; //краткое название

    private String description; //развёрнутое описание

    private Boolean available; //статус о том, доступна или нет вещь для аренды

    private User owner; //владелец вещи

    private ItemRequest itemRequest; //если вещь была создана по запросу другого пользователя, то храним его

    private List<CommentDto> comments = new ArrayList<>(); //список комментариев к вещи

    public ItemOutDto(Long id, String name, String description, Boolean available, User owner, ItemRequest itemRequest) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.itemRequest = itemRequest;
        this.comments = new ArrayList<>();
    }
}