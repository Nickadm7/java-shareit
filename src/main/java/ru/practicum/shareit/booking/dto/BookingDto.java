package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id; //уникальный идентификатор бронирования

    private LocalDate start; //дата и время начала бронирования

    private LocalDate end; //дата и время конца бронирования

    private ItemDto itemDto; //вещь, которую пользователь бронирует

    private UserDto userDto; //пользователь, который осуществляет бронирование

    private Status status; //статус бронирования
}