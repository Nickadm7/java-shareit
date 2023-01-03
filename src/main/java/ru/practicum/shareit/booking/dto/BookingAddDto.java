package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAddDto {
    private Long id; //уникальный идентификатор бронирования

    @FutureOrPresent
    private LocalDate start; //дата и время начала бронирования

    @Future
    private LocalDate end; //дата и время конца бронирования

    private Long itemId; //id вещи, которую пользователь бронирует
}
