package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllBookings_BybookerId_OrderByEndDesc(Long ownerId);

    List<Booking> findAllBookings_BybookerId_OrderByEndDesc(Long ownerId, Pageable pageable);

    List<Booking> findAllBookings_ByBookerIdAndStatus_OrderByEndDesc(Long userId, Status status);

    List<Booking> findByItem_OwnerId_OrderByEndDesc(Long ownerId);

    List<Booking> findByItem_OwnerId_OrderByEndDesc(Long ownerId, Pageable pageable);

    List<Booking> findByItem_OwnerIdAndStatus_OrderByEndDesc(Long userId, Status status);

    List<Booking> findAllBookingsByBookerIdAndItemId(Long bookerId, Long itemId);

    Booking findFirstByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Booking findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);
}