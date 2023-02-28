package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enam.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking dtoToBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                bookingDto.getItem(), bookingDto.getBooker(), bookingDto.getStatus());
    }

    public static BookingDto bookingToDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(),
                booking.getEnd(), booking.getItem(), booking.getBooker(), booking.getStatus());
    }

    public static Booking createNewBooking(InputBookingDto inputBookingDto, User user, Item item){
        return Booking.builder()
                .start(inputBookingDto.getStart())
                .end(inputBookingDto.getEnd())
                .item(item)
                .booker(user)
                .status(StatusBooking.WAITING)
                .build();
    }
}

