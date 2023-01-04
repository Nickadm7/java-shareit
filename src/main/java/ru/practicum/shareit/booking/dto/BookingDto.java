package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id; //уникальный идентификатор бронирования

    @FutureOrPresent
    private LocalDateTime start; //дата и время начала бронирования

    @Future
    private LocalDateTime end; //дата и время конца бронирования

    private Item item; //вещь, которую пользователь бронирует

    private User booker; //пользователь, который осуществляет бронирование

    private Status status; //статус бронирования
}