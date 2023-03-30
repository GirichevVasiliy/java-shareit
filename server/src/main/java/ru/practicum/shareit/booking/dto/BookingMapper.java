package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingDto bookingToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toItemDtoSingl(booking.getItem()))
                .booker(UserMapper.userToDto(booking.getBooker()))
                .status(booking.getStatus().name())
                .build();
    }

    public static Booking createNewBooking(InputBookingDto inputBookingDto, User user, Item item) {
        return Booking.builder()
                .start(inputBookingDto.getStart())
                .end(inputBookingDto.getEnd())
                .item(item)
                .booker(user)
                .status(StatusBooking.WAITING)
                .build();
    }

    public static DateBookingDto toDateBookingDto(Booking booking) {
        return DateBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}

