package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllBookingsBybookerIdOrderByEndDesc(Long ownerId);

    List<Booking> findAllBookingsByBookerIdAndStatusOrderByEndDesc(Long userId, Status status);

}