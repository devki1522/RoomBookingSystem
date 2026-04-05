package com.booking.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {
    @Test
    public void testSuccessfulBookingApproval(){

        User student = new User("Devki", "Student");
        Room lab = new Room("Lab_01" , 30);
        Booking booking = new Booking("B001", student, lab, LocalDateTime.now(), LocalDateTime.now());

        booking.approve();

        assertEquals(BookingStatus.APPROVED, booking.getStatus());
        System.out.println("Test 01 Passed. Booking is now : " + booking.getStatus());

    }

    @Test
    public void testInvalidStateTransition(){

        User student = new User("Devki", "Student");
        Room lab = new Room("Lab_01" , 30);
        Booking booking = new Booking("B002", student, lab, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        booking.approve();

        booking.approve();
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
        System.out.println("Test 02 Passed.  : Handled Invalid transition. " );
    }
}
