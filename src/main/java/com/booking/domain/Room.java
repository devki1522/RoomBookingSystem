package com.booking.domain;

public class Room {
    private String roomId;
    private int capacity;
    private boolean requiresApproval;

    //basic constructor
    public Room(String roomId, int capacity,  boolean requiresApproval) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.requiresApproval = requiresApproval;
    }

    //getters
    public String getRoomId(){
        return roomId;
    }

    public int getCapacity(){
        return capacity;
    }

    public boolean isRequiresApproval(){
        return requiresApproval;
    }
}
