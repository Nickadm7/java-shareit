package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Long id; //уникальный идентификатор бронирования

    private LocalDate start; //дата и время начала бронирования

    private LocalDate end; //дата и время конца бронирования

    private Item item; //вещь, которую пользователь бронирует

    private User booker; //пользователь, который осуществляет бронирование

    private Status status; //статус бронирования
}