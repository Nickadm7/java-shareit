package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {
    private BookingMapper() {
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static BookingDto toBookingAddDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                Status.WAITING
        );

    }

    public static Booking toBooking(BookingDto bookingDto, User owner) { //TODO
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItem(),
                bookingDto.getBooker(),
                bookingDto.getStatus()
        );
    }

    public static Booking toBooking(BookingAddDto bookingAddDto, Item item, User owner) {
        Booking booking = new Booking();
        booking.setStart(bookingAddDto.getStart());
        booking.setEnd(bookingAddDto.getEnd());
        booking.setItem(item);
        booking.setBooker(owner);
        booking.setStatus(Status.WAITING);
        return booking;
    }

    public static BookingAddDto toAddBookingDto(Booking booking) {
        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setId(booking.getId());
        bookingAddDto.setItemId(booking.getItem().getId());
        bookingAddDto.setStart(booking.getStart());
        bookingAddDto.setEnd(booking.getEnd());
        return bookingAddDto;
    }
}
