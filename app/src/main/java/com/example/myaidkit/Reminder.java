package com.example.myaidkit;

public class Reminder {
    int id;
    String name;
    Float quantity;
    Long time;

    public Reminder(int id, String name, Float quantity, Long time) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.time = time;
    }

    public Reminder(String name, Float quantity, Long time) {
        this.name = name;
        this.quantity = quantity;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getQuantity() {
        return quantity;
    }

    public Long getTime() {
        return time;
    }
}
