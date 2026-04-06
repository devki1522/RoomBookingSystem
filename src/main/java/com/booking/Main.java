package com.booking;

import com.booking.application.BookingService;
import com.booking.domain.Room;
import com.booking.domain.User;
import com.booking.infrastructure.InMemoryBookingRepository;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //setup database and service
        InMemoryBookingRepository bookingRepository = new InMemoryBookingRepository();
        BookingService bookingService = new BookingService(bookingRepository);

        //sample data for demo
        Room room1 = new Room("Room1", 10, false);
        Room room2 = new Room("Room2", 10, true);

        bookingRepository.addRoom(room1);
        bookingRepository.addRoom(room2);

        Scanner scanner = new Scanner(System.in);
        User curentUser = new User("Devki", "Student");

        System.out.println("Room Booking System.");
        boolean running = true;
        while (running){
            System.out.println("\n1. View Available Rooms.\n2. Create Booking\n3. Exit.");
            System.out.println("Select an option:");
            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    List<Room> allRooms = bookingRepository.findAllRooms();
                    for(Room r : allRooms){
                        System.out.println("ID : " + r.getRoomId() + " Capacity : " + r.getCapacity());
                    }
                    break;
                    case 2:
                        System.out.println("Enter Room ID");
                        String roomId = scanner.next();
                        Room room = bookingRepository.findAllRooms().stream().filter(r -> r.getRoomId().equals(roomId)).findFirst().orElse(null);

                        if(room == null){
                            System.out.println("Room Not Found");
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
                            running = false;
                            break;
                            default:
                                System.out.println("Wrong choice");

            }
        }
        scanner.close();

    }

}
