package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //уникальный идентификатор бронирования

    @Column(name = "start_date")
    private LocalDateTime start; //дата и время начала бронирования

    @Column(name = "end_date")
    private LocalDateTime end; //дата и время конца бронирования

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item; //вещь, которую пользователь бронирует

    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker; //пользователь, который осуществляет бронирование

    @Enumerated(EnumType.STRING)
    private Status status; //статус бронирования
}