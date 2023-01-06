package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllBookingsBybookerIdOrderByEndDesc(Long ownerId);

    List<Booking> findAllBookingsByBookerIdAndStatusOrderByEndDesc(Long userId, Status status);

    List<Booking> findByItem_Owner_Id_OrderByEndDesc(Long ownerId);

    List<Booking> findByItem_Owner_Id_AndStatusOrderByEndDesc(Long userId, Status status);

    List<Booking> findAllBookingsByBookerIdAndItemId(Long bookerId, Long itemId);




}