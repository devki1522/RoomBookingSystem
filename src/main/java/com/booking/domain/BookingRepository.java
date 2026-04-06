package com.booking.domain;

import java.util.List;

public interface BookingRepository {
    void save(Booking booking);
    List<Booking> findAll();
    List<Booking> findByRoomId(String roomId);
}
