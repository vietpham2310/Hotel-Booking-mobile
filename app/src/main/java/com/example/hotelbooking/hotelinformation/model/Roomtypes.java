package com.example.hotelbooking.hotelinformation.model;

public class Roomtypes {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String avatar;
    private boolean isActive;
    private int hotelId;

    public Roomtypes(int id, String name, double price, int quantity, String avatar, boolean isActive, int hotelId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.avatar = avatar;
        this.isActive = isActive;
        this.hotelId = hotelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
}
