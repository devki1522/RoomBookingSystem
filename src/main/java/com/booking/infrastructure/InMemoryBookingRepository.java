package com.booking.infrastructure;

import com.booking.domain.Booking;
import com.booking.domain.BookingRepository;
import com.booking.domain.BookingStatus;
import com.booking.domain.Room;
import com.booking.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;

public class InMemoryBookingRepository implements BookingRepository {
    private List<Booking> database = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    @Override
    public void save(Booking booking) {
        database.add(booking);
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(database);
    }

    @Override
    public List<Booking> findByRoomId(String roomId) {
        return database.stream()
                .filter(b -> b.getRoom().getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAllRooms() {
        return new ArrayList<>(rooms);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void saveRoomsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("rooms.txt"))) {
            for (Room room : rooms) {
                writer.println(room.getRoomId() + " , " + room.getCapacity() + " , " + room.isRequiresApproval());
            }
            System.out.println("[REQ-2.3] SUCCESS: Rooms saved to rooms.txt");
        } catch (IOException ioe) {
            System.out.println("Error saving rooms: " + ioe.getMessage());
        }
    }

    public void loadRoomsFromFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("rooms.txt"));
            rooms.clear();
            for (String line : lines) {
                String[] parts = line.split(" , ");
                rooms.add(new Room(parts[0], Integer.parseInt(parts[1]), Boolean.parseBoolean(parts[2])));
            }
            System.out.println("[REQ-2.3] SUCCESS: Rooms loaded from rooms.txt");
        } catch (IOException ioe) {
            System.out.println("Error loading rooms.");
        }
    }

    public void saveBookingsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("bookings.txt"))) {
            for (Booking booking : database) {
                writer.println(
                        booking.getBookingId() + " , " +
                                booking.getRequester().getName() + " , " +
                                booking.getRequester().getRole() + " , " +
                                booking.getRoom().getRoomId() + " , " +
                                booking.getStartTime() + " , " +
                                booking.getEndTime() + " , " +
                                booking.getStatus()
                );
            }
            System.out.println("[REQ-2.3] SUCCESS: Bookings saved to bookings.txt");
        } catch (IOException ioe) {
            System.out.println("Error saving bookings: " + ioe.getMessage());
        }
    }

    public void loadBookingsFromFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("bookings.txt"));
            database.clear();

            for (String line : lines) {
                String[] parts = line.split(" , ");

                String bookingId = parts[0];
                String userName = parts[1];
                String userRole = parts[2];
                String roomId = parts[3];
                LocalDateTime start = LocalDateTime.parse(parts[4]);
                LocalDateTime end = LocalDateTime.parse(parts[5]);
                BookingStatus status = BookingStatus.valueOf(parts[6]);

                Room matchedRoom = rooms.stream()
                        .filter(r -> r.getRoomId().equals(roomId))
                        .findFirst()
                        .orElse(null);

                if (matchedRoom != null) {
                    User user = new User(userName, userRole);
                    Booking booking = new Booking(bookingId, user, matchedRoom, start, end, status);
                    database.add(booking);
                }
            }

            System.out.println("[REQ-2.3] SUCCESS: Bookings loaded from bookings.txt");
        } catch (IOException ioe) {
            System.out.println("Error loading bookings.");
        }
    }
}