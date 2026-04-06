package com.booking.domain;

import java.util.LinkedList;
import java.util.Queue;

public class Room {
    private String roomId;
    private int capacity;
    private boolean requiresApproval;
    private Queue<User> waitlist =  new LinkedList<>();

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

    public Queue<User> getWaitlist() {
        return waitlist;
    }

    public int getCapacity(){
        return capacity;
    }

    public boolean isRequiresApproval(){
        return requiresApproval;
    }

    public void addToWaitlist(User user){
        waitlist.add(user);
    }

    public void removeFromWaitlist(User user){
        waitlist.remove(user);
    }

    public User peekWaitlist(){
        return waitlist.peek();
    }

    public int getWaitlistSize(){
        return waitlist.size();
    }
}
