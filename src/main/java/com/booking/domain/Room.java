package com.booking.domain;

public class Room {
    private String roomId;
    private int capacity;

    //basic constructor
    public Room(String roomId, int capacity){
        this.roomId = roomId;
        this.capacity = capacity;
    }

    //getters
    public String getRoomId(){
        return roomId;
    }

    public int getCapacity(){
        return capacity;
    }
}
