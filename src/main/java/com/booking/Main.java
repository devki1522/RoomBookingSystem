package com.booking;

import com.booking.application.BookingService;
import com.booking.domain.Booking;
import com.booking.domain.BookingStatus;
import com.booking.domain.Room;
import com.booking.domain.User;
import com.booking.infrastructure.InMemoryBookingRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //setup database and service
        InMemoryBookingRepository bookingRepository = new InMemoryBookingRepository();
        BookingService bookingService = new BookingService(bookingRepository);

        bookingRepository.loadRoomsFromFile();
        bookingRepository.loadBookingsFromFile();

        if(bookingRepository.findAllRooms().isEmpty()){
            bookingRepository.addRoom(new Room("Room1", 10, false));
            bookingRepository.addRoom(new Room("Room2", 10, true));
            bookingRepository.saveRoomsToFile();
        }

        Scanner scanner = new Scanner(System.in);
        User curentUser = new User("Devki", "Student");

        System.out.println("[REQ-2.2] Architecture Summary");
        System.out.println("Presentation Layer: Main.java (CLI menu and user interaction)");
        System.out.println("Application Layer: BookingService.java (business logic and workflow)");
        System.out.println("Domain Layer: Booking, Room, User, BookingStatus");
        System.out.println("Infrastructure Layer: InMemoryBookingRepository (storage and file persistence)");
        System.out.println("Dependency Direction: Main -> BookingService -> Domain -> Repository");

        System.out.println("[REQ-2.7] Pattern Map");
        System.out.println("Creational Pattern: BookingFactory (creates booking objects)");
        System.out.println("Structural Pattern: BookingRepository / InMemoryBookingRepository (decouples logic from storage)");
        System.out.println("Behavioral Pattern 1: BookingStatus lifecycle handling (approval workflow)");
        System.out.println("Behavioral Pattern 2: Validation/approval behavior inside BookingService workflow");

        System.out.println("[REQ-2.1] Room Booking System CLI started successfully.");
        boolean running = true;
        while (running){

            System.out.println("1. View Available Rooms");
            System.out.println("2. Create Booking (REQ-2.8 Validation)");
            System.out.println("3. View All Bookings (REQ-2.4 Reporting)");
            System.out.println("4. Admin: Approve Bookings (REQ-2.5 Lifecycle)");
            System.out.println("5. Add New Room (REQ-2.3 Persistence)");
            System.out.println("6. Performance Test (REQ-2.9)");
            System.out.println("7. Policy Comparison (REQ-2.6)");
            System.out.println("8. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    System.out.println("[REQ-2.4] Query 1: Available Rooms Report");
                    List<Room> allRooms = bookingRepository.findAllRooms();
                    for(Room r : allRooms){
                        System.out.println("ID : " + r.getRoomId() + " Capacity : " + r.getCapacity());
                    }
                    System.out.println();
                    break;

                    case 2:
                        System.out.println("Enter Room ID");
                        String roomId = scanner.next();
                        Room room = bookingRepository.findAllRooms().stream()
                                .filter(r -> r.getRoomId().equals(roomId))
                                .findFirst().orElse(null);

                        if(room == null){
                            System.out.println("Room Not Found. (REQ-2.8 - error handling/messages");
                            break;
                        }
                        System.out.println("Enter Number of Attendees: ");
                        int attendees = scanner.nextInt();

                        LocalDateTime startTime = LocalDateTime.now();
                        LocalDateTime endTime = startTime.plusHours(1);

                        String result = bookingService.createBookingRequest(curentUser, room, startTime, endTime, attendees);
                        System.out.println(result);

                        if (result.startsWith("SUCCESS")) {
                            bookingRepository.saveBookingsToFile();
                        }
                        System.out.println();
                        break;
                        case 3:
                            System.out.println("[REQ-2.4] Query 2: Booking Status Report");
                            List<Booking> bookings = bookingRepository.findAll();
                            if(bookings.isEmpty()){
                                System.out.println("No bookings found for the current report.");
                            }
                            for(Booking b : bookings){
                                System.out.println("[ " + b.getStatus() + " ] ID: " + b.getBookingId() + " | Room: " + b.getRoom().getRoomId());
                            }
                            System.out.println();
                            break;
                case 4:
                    System.out.println("[REQ-2.5] Lifecycle Demo: Admin Approval Portal");
                    bookingRepository.findAll().stream()
                            .filter(b -> b.getStatus() == BookingStatus.PENDING_APPROVAL)
                            .forEach(b -> {
                                System.out.println("Before approval: " + b.getStatus());
                                System.out.println("Approve : " + b.getBookingId() + " for Room ID : " + b.getRoom().getRoomId() + "? [y/n]");
                                if(scanner.next().equalsIgnoreCase("y")){
                                    b.approve();
                                    System.out.println("After approval: " + b.getStatus());

                                    System.out.println("[REQ-2.5] Invalid transition test: approving already approved booking again");
                                    b.approve();

                                    bookingRepository.saveBookingsToFile();
                                }
                            });
                    System.out.println();
                    break;
                                case 5:
                                    System.out.println("add New Room:");
                                    String newRoomId = scanner.next();
                                    System.out.println("Enter Capaciy: ");
                                    int capacity = scanner.nextInt();
                                    System.out.println("Restricted: (true/false)");
                                    boolean restricted = scanner.nextBoolean();

                                    bookingRepository.addRoom(new Room(newRoomId, capacity, restricted));
                                    bookingRepository.saveRoomsToFile();
                                    System.out.println("Room added and Saved to file. ");
                                    System.out.println();
                                    break;
                case 6:
                    System.out.println("[REQ-2.9] Performance Test");

                    long start = System.nanoTime();

                    List<Booking> allBookingsForPerf = bookingRepository.findAll();
                    int count = 0;
                    for (Booking b : allBookingsForPerf) {
                        if (b.getRoom().getRoomId() != null) {
                            count++;
                        }
                    }

                    long end = System.nanoTime();
                    long durationMs = (end - start) / 1_000_000;

                    System.out.println("Dataset size: " + allBookingsForPerf.size() + " bookings");
                    System.out.println("Search/report completed in " + durationMs + " ms");
                    System.out.println("Trade-off: In-memory search is simple and fast for small datasets, but indexed or database-based search would scale better for larger systems.");
                    System.out.println();
                    break;

                case 7:
                    System.out.println("[REQ-2.6] Policy Comparison Demo");

                    Room standardRoom = bookingRepository.findAllRooms().stream()
                            .filter(r -> !r.isRequiresApproval())
                            .findFirst()
                            .orElse(null);

                    Room restrictedRoom = bookingRepository.findAllRooms().stream()
                            .filter(Room::isRequiresApproval)
                            .findFirst()
                            .orElse(null);

                    if (standardRoom != null) {
                        Booking standardBooking = new Booking(
                                "POLICY-A",
                                curentUser,
                                standardRoom,
                                LocalDateTime.now(),
                                LocalDateTime.now().plusHours(1)
                        );
                        System.out.println("Policy A: Standard Room Booking");
                        System.out.println("Room ID: " + standardRoom.getRoomId());
                        System.out.println("Requires Approval: " + standardRoom.isRequiresApproval());
                        System.out.println("Resulting Status: " + standardBooking.getStatus());
                    } else {
                        System.out.println("Policy A could not be demonstrated because no standard room was found.");
                    }

                    if (restrictedRoom != null) {
                        Booking restrictedBooking = new Booking(
                                "POLICY-B",
                                curentUser,
                                restrictedRoom,
                                LocalDateTime.now(),
                                LocalDateTime.now().plusHours(1)
                        );
                        System.out.println("Policy B: Restricted Room Booking");
                        System.out.println("Room ID: " + restrictedRoom.getRoomId());
                        System.out.println("Requires Approval: " + restrictedRoom.isRequiresApproval());
                        System.out.println("Resulting Status: " + restrictedBooking.getStatus());
                    } else {
                        System.out.println("Policy B could not be demonstrated because no restricted room was found.");
                    }

                    System.out.println("Explanation: Standard rooms follow direct request policy, while restricted rooms follow approval-required policy.");
                    System.out.println();
                    break;

                case 8:
                    running = false;
                    break;
                            default:
                                System.out.println("Wrong choice");

            }
        }
        scanner.close();

    }

}
