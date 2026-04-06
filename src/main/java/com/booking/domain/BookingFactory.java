package com.booking.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class BookingFactory {
    public static Booking createStandardBooking(String id, User user, Room room, LocalDateTime start, LocalDateTime end){
        return new Booking(id, user, room, start, end);
    }

    public static List<Booking> createRecurringBookings(String id, User user, Room room, LocalDateTime start, LocalDateTime end, RecurrenceRule rule){
        List<Booking> series = new ArrayList<>();

        for (int i = 0; i< rule.getTotalOccurrences(); i++){
            LocalDateTime occurrenceStart = start.plusDays((long) i * rule.getDaysBetween());
            LocalDateTime occurrenceEnd = end.plusDays((long) rule.getDaysBetween());

            String occurrenceId = id +"_" + (i+1);

            series.add(new Booking(occurrenceId, user, room, occurrenceStart, occurrenceEnd));
        }
        return series;
    }

}
