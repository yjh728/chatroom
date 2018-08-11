package com.example.yjh.chartroom;

public class User {
    private String username;
    private String  password;

    public User(String account, String password) {
        this.username = account;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
