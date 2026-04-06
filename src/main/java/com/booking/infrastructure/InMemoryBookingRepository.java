package com.booking.infrastructure;

import com.booking.domain.Booking;
import com.booking.domain.BookingRepository;
import com.booking.domain.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.*;

public class InMemoryBookingRepository implements BookingRepository {
    private List<Booking> database = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    @Override
    public void save(Booking booking){
        database.add(booking);
    }

    @Override
    public List<Booking> findAll(){
        return new ArrayList<>(database);
    }

    @Override
    public List<Booking> findByRoomId(String roomId){
        return database.stream()
                .filter(b -> b.getRoom().getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAllRooms(){
        return new ArrayList<>(rooms);
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    public void saveRoomsToFile(){
        try(PrintWriter writer = new PrintWriter(new FileWriter("rooms.txt"))){
            for(Room room : rooms){
                //Format = roomId, Capacity, Restriction
                writer.println(room.getRoomId() + " , " + room.getCapacity() + " , " + room.isRequiresApproval());
            }
            System.out.println("[REQ -2.3] SUCCESS : Rooms saved to rooms.txt");

        } catch(IOException ioe){
            System.out.println("Error saving rooms: " + ioe.getMessage());
        }
    }

    public void loadRoomsFromFile(){
        try{
            List<String> lines = Files.readAllLines(Paths.get("rooms.txt"));
            rooms.clear();
            for(String line : lines){
                String[] parts = line.split(" , ");
                rooms.add(new Room(parts[0], Integer.parseInt(parts[1]), Boolean.parseBoolean(parts[2])));

            }
            System.out.println("[REQ -2.3] SUCCESS : Rooms loaded from rooms.txt");
        } catch(IOException ioe){
            System.out.println("Error loading rooms.");
        }
    }
}
