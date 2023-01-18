package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOutForFindDto {
    private Long id; //уникальный идентификатор вещи

    private String name; //краткое название

    private String description; //развёрнутое описание

    private Boolean available; //статус о том, доступна или нет вещь для аренды

    private BookingOutDto lastBooking;

    private BookingOutDto nextBooking;

    private List<CommentOutDto> comments = new ArrayList<>(); //список комментариев к вещи
}