package com.booking.infrastructure;

import com.booking.domain.Booking;
import com.booking.domain.BookingRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryBookingRepository implements BookingRepository {
    private List<Booking> database = new ArrayList<>();

    @Override
    public void save(Booking booking){
        database.add(booking);
    }

    @Override
    public List<Booking> findAll(){
        return new ArrayList<>(database);
    }

    @Override
    public List<Booking> findByRoomId(String roomId){
        return database.stream()
                .filter(b -> b.getRoom().getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }
}
