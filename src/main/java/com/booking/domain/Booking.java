package com.booking.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
    private String bookingId;
    private User requester;
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;

    // constructor

    public Booking(String bookingId, User requester, Room room, LocalDateTime startTime, LocalDateTime endTime){
        this.bookingId = bookingId;
        this.requester = requester;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = BookingStatus.REQUESTED; // this will be the default state
    }

    public void approve(){
        if(this.status == BookingStatus.REQUESTED || this.status == BookingStatus.PENDING_APPROVAL){
            this.status = BookingStatus.APPROVED;
        }
        else{
            System.out.println("Error: Cannot approve a booking in " + this.status + "state.");
        }
    }

    public BookingStatus getStatus(){
        return status;
    }
}

