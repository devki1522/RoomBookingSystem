package com.booking.domain;

public class User {
    private String name;
    private String role;

    //constructor

    public User(String name, String role){
        this.name = name;
        this .role = role;
    }

    //Getters
    public String getName(){
        return name;
    }
    public String getRole(){
        return role;
    }
}
