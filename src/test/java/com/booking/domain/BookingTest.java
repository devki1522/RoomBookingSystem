package com.booking.domain;

import com.booking.application.BookingService;
import com.booking.infrastructure.InMemoryBookingRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {
    @Test
    public void testSuccessfulBookingApproval(){

        User student = new User("Devki", "Student");
        Room lab = new Room("Lab_01" , 30, false);
        Booking booking = new Booking("B001", student, lab, LocalDateTime.now(), LocalDateTime.now());

        booking.approve();

        assertEquals(BookingStatus.APPROVED, booking.getStatus());
        System.out.println("Test Passed. Booking is now : " + booking.getStatus());

    }

    @Test
    public void testInvalidStateTransition(){

        User student = new User("Devki", "Student");
        Room lab = new Room("Lab_01" , 30, false);
        Booking booking = new Booking("B002", student, lab, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        booking.approve();

        booking.approve();
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
        System.out.println("Test Passed.  : Handled Invalid transition. " );
    }

    @Test
    public void testRoomConflictDetection(){

        User devki = new User("Devki", "Student");
        Room lab = new Room("Lab_01" , 30, false);

        //first booking
        LocalDateTime start1 = LocalDateTime.of(2026,5,1,10,0);
        LocalDateTime end1 = LocalDateTime.of(2026,5,1,11,0);
        Booking booking1 = new Booking("B1", devki, lab, start1, end1);

        //second booking ---Trying to overlap with the first one
        LocalDateTime start2 = LocalDateTime.of(2026,5,1,10,30);
        LocalDateTime end2 = LocalDateTime.of(2026,5,1,11,30);
        Booking booking2 = new Booking("B2", devki, lab, start2, end2);

        assertTrue(booking1.conflictsWith(booking2), "The bookings should conflict!");
        System.out.println("Test Passed:  Conflict correctly detected. ");
    }

    @Test
    public void testServiceBlockDoubleBooking(){
        BookingRepository repository = new InMemoryBookingRepository();
        BookingService service = new BookingService(repository);
        User devki = new User("Devki", "Student");
        Room lab = new Room("Lab_01" , 30, false);

        LocalDateTime start1 = LocalDateTime.of(2026,5,1,10,0);
        LocalDateTime end1 = LocalDateTime.of(2026,5,1,11,0);

        String result1 = service.createBookingRequest(devki, lab, start1, end1, 10);
        assertEquals("SUCCESS : Booking confirmed.", result1);

        String result2 = service.createBookingRequest(devki, lab, start1, end1, 10);
        assertEquals("Booking already exists", result2);

        System.out.println("Service test passed.");
    }

    @Test
    public void testRoomRequiresApproval(){
        BookingRepository repository = new InMemoryBookingRepository();
        BookingService service = new BookingService(repository);
        User devki = new User("Devki", "Student");
        Room lab = new Room("Lab_01" , 30, true); //true because this needs approval

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        String result = service.createBookingRequest(devki, lab, start, end, 10);

        assertEquals("SUCCESS : Booking Submitted and awaiting Admin Approval", result);
        System.out.println("Restrcited Room Test Passed.");
    }

    @Test
    public void testRoomCapacityExceeded() {
        BookingRepository repository = new InMemoryBookingRepository();
        BookingService service = new BookingService(repository);
        User devki = new User("Devki", "Student");

        // Room only fits 10 people
        Room smallRoom = new Room("Small_01", 10, false);

        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = start.plusHours(1);

        // Try to book for 50 people
        String result = service.createBookingRequest(devki, smallRoom, start, end, 50);

        assertEquals("REJECTED: Room capacity exceeded.", result);
        System.out.println("Test Passed: System blocked over-capacity booking.");
    }

    @Test
    public void testRecurringBookingCreation() {
        BookingRepository repo = new InMemoryBookingRepository();
        BookingService service = new BookingService(repo);
        User devki = new User("Devki", "Student");
        Room lab = new Room("Lab_01", 30, false);

        // Weekly for 4 weeks
        RecurrenceRule weeklyFourWeeks = new RecurrenceRule(4, 7);

        LocalDateTime start = LocalDateTime.of(2026, 7, 1, 10, 0);
        LocalDateTime end = start.plusHours(2);

        String result = service.createRecurringBookingRequest(devki, lab, start, end, 10, weeklyFourWeeks);

        assertEquals("SUCCESS : Created 4 recurring bookings.", result);
        assertEquals(4, repo.findAll().size(), "There should be 4 individual bookings in the repository.");
        System.out.println("Test Passed : Recurring series expanded and saved correctly.");
    }
}
