package com.model;

public class Hero {
    private Long id;
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Hero(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
