package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAddDto {
    private Long id; //уникальный идентификатор бронирования
    private Long itemId; //id вещи, которую пользователь бронирует
    @FutureOrPresent
    private LocalDateTime start; //дата и время начала бронирования
    @Future
    private LocalDateTime end; //дата и время конца бронирования
}
