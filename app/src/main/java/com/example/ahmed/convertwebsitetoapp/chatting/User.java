package com.example.ahmed.convertwebsitetoapp.chatting;

public class User {
 
    private int id;
    private String username, email, password;
 
    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
 
    public int getId() {
        return id;
    }
 
    public String getUsername() {
        return username;
    }
 
    public String getEmail() {
        return email;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getGender() {
        return password;
    }
}