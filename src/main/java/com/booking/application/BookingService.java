package com.booking.application;

import com.booking.domain.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    // Instead of a List, we use the Repository Interface
    private final BookingRepository repository;

    // We "Inject" the repository through the constructor
    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public String createBookingRequest(User user, Room room, LocalDateTime start, LocalDateTime end, int attendees){

        //check number of attendees , shouldn't exceed total capacity
        if (attendees > room.getCapacity()) {
            return "REJECTED: Room capacity exceeded.";
        }

        Booking newRequest = BookingFactory.createStandardBooking("B-" + (repository.findAll().size() + 1), user, room, start, end);

        // Check for conflicts using the repository
        List<Booking> existingBookings = repository.findByRoomId(room.getRoomId());
        for (Booking existing : existingBookings) {
            if (newRequest.conflictsWith(existing)) {
                room.addToWaitlist(user);
                return "REJECTED: Booking already exists. You have been added to the waitlist.";
            }
        }

        repository.save(newRequest);

        if (newRequest.getStatus() == BookingStatus.PENDING_APPROVAL) {
            return "SUCCESS : Booking Submitted and awaiting Admin Approval";
        } else {
            return "SUCCESS : Booking confirmed.";
        }
    }

    public String createRecurringBookingRequest(User user, Room room, LocalDateTime start, LocalDateTime end,
                                                int attendees, RecurrenceRule rule){
        if (attendees > room.getCapacity()) {
            return "REJECTED: Room capacity exceeded.";
        }

        List<Booking> series = BookingFactory.createRecurringBookings("RB-" + (repository.findAll().size() + 1), user, room, start, end, rule);

        for (Booking newOccurence : series) {
            List<Booking> existing = repository.findByRoomId(room.getRoomId());
            for(Booking b : existing) {
                if (newOccurence.conflictsWith(b)) {
                    room.addToWaitlist(user);
                    return "Room is already booked for this time." + newOccurence.getStartTime().toLocalDate() + " Added to waitlist.";
                }
            }
        }

        for (Booking booking : series) {
            repository.save(booking);
        }
        return "SUCCESS : Created " + series.size() + " recurring bookings.";
    }

    public List<Room> findAvailableRooms(LocalDateTime start, LocalDateTime end, int attendees){
        List<Room> allRooms = repository.findAllRooms();

        return allRooms.stream()
                .filter(room -> room.getCapacity() >= attendees)
                .filter(room -> isRoomFree(room, start, end))
                .collect(java.util.stream.Collectors.toList());
    }

    private boolean isRoomFree(Room room, LocalDateTime start, LocalDateTime end){
        List<Booking> bookings = repository.findByRoomId(room.getRoomId());

        for (Booking b: bookings){
            if(b.getStartTime().isBefore(end) && start.isBefore(b.getEndTime())){
                return false;
            }
        }
        return true;
    }

}