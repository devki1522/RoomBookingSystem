package com.booking.domain;

import com.booking.application.BookingService;
import com.booking.infrastructure.InMemoryBookingRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

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
        assertEquals("REJECTED: Booking already exists. You have been added to the waitlist.", result2);

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

    @Test
    public void testWaitlistPlacement(){
        BookingRepository repo = new InMemoryBookingRepository();
        BookingService service = new BookingService(repo);
        User user1 = new User("Alice", "Student");
        User user2 = new User("Devki", "Student");
        Room lab = new Room("Lab_01", 10, false);

        LocalDateTime time = LocalDateTime.of(2026, 8, 1, 12, 0);

        // 1. Alice books the room successfully
        service.createBookingRequest(user1, lab, time, time.plusHours(1), 5);

        // 2. Devki tries to book the SAME room/time
        String result = service.createBookingRequest(user2, lab, time, time.plusHours(1), 5);

        // 3. Assertions
        assertTrue(result.contains("added to the waitlist"));
        assertEquals(1, lab.getWaitlistSize());
        assertEquals("Devki", lab.peekWaitlist().getName());

        System.out.println("Test Passed: User successfully queued in waitlist.");
    }

    @Test
public void testFindAvailableRooms() {
    InMemoryBookingRepository repo = new InMemoryBookingRepository();
    BookingService service = new BookingService(repo);

    Room roomA = new Room("Large_Room", 50, false);
    Room roomB = new Room("Small_Room", 5, false);
    repo.addRoom(roomA);
    repo.addRoom(roomB);

    LocalDateTime start = LocalDateTime.of(2026, 9, 1, 10, 0);
    LocalDateTime end = start.plusHours(1);

    // 1. Search for a room for 10 people (Both should show up)
    List<Room> available = service.findAvailableRooms(start, end, 10);
    assertEquals(1, available.size(), "Only Large_Room should fit 10 people.");
    assertEquals("Large_Room", available.get(0).getRoomId());

    // 2. Book the Large_Room
    User user = new User("Devki", "Student");
    service.createBookingRequest(user, roomA, start, end, 10);

    // 3. Search again for the SAME time (Large_Room should now be hidden)
    List<Room> availableNow = service.findAvailableRooms(start, end, 10);
    assertTrue(availableNow.isEmpty(), "No rooms should be available for 10 people now.");
}
}
