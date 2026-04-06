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

        if(bookingRepository.findAllRooms().isEmpty()){
            bookingRepository.addRoom(new Room("Room1", 10, false));
            bookingRepository.addRoom(new Room("Room2", 10, true));
            bookingRepository.saveRoomsToFile();
        }

        Scanner scanner = new Scanner(System.in);
        User curentUser = new User("Devki", "Student");

        System.out.println("Room Booking System. (REQ-2.1 a simple CLI menu.)");
        boolean running = true;
        while (running){

            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Create Booking (REQ-2.8 Validation)");
            System.out.println("3. View All Bookings (REQ-2.4 Reporting)");
            System.out.println("4. Admin: Approve Bookings (REQ-2.5 Lifecycle)");
            System.out.println("5. Add New Room (REQ-2.3 Persistence)");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Current Room availability.(REQ-2.4 - Trivial Query.)");
                    List<Room> allRooms = bookingRepository.findAllRooms();
                    for(Room r : allRooms){
                        System.out.println("ID : " + r.getRoomId() + " Capacity : " + r.getCapacity());
                    }
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
                        break;
                        case 3:
                            System.out.println("List of all bookings. (REQ-2.4 - Trivial Query.)");
                            List<Booking> bookings = bookingRepository.findAll();
                            if(bookings.isEmpty()){
                                System.out.println("No bookings found.");
                            }
                            for(Booking b : bookings){
                                System.out.println("[ " + b.getStatus() + " ] ID: " + b.getBookingId() + " | Room: " + b.getRoom().getRoomId());
                            }
                            break;
                            case 4:
                                System.out.println("AdminApproval Portal:(REQ-2.5 - state change.)");
                                bookingRepository.findAll().stream()
                                        .filter(b -> b.getStatus() == BookingStatus.PENDING_APPROVAL)
                                        .forEach(b -> {
                                            System.out.println("Approve : " + b.getBookingId() + " for Room ID : " + b.getRoom().getRoomId() + "? [y/n]");
                                            if(scanner.next().equalsIgnoreCase("y")){
                                                b.approve();
                                            }
                                        });
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
                                    break;
                                    case 6:
                                        running = false;
                                        break;
                            default:
                                System.out.println("Wrong choice");

            }
        }
        scanner.close();

    }

}
