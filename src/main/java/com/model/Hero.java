package com.model;

public class Hero {
    private Integer id;
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Hero(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
