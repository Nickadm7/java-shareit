package ru.practicum.shareit.booking.model;

import java.time.LocalDate;

public class Booking {
    private Long id; //уникальный идентификатор бронирования

    private LocalDate start; //дата и время начала бронирования

    private LocalDate end; //дата и время конца бронирования

    private Long item; //id вещи, которую пользователь бронирует

    private Long booker; //id пользователя, который осуществляет бронирование

    private Status status; //статус бронирования
}