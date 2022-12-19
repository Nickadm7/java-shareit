package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id; //уникальный идентификатор вещи
    @NotBlank
    private String name; //краткое название
    @NotBlank
    private String description; //развёрнутое описание
    @NotNull
    private Boolean available; //статус о том, доступна или нет вещь для аренды

    private Long owner; //id владелеца веще

    private Boolean request; //если вещь была создана по запросу другого пользователя, то храним его id
}
