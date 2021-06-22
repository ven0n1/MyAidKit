package com.example.myaidkit;

public class Reminder {
    String name;
    Float quantity;
    String time;

    public Reminder(String name, Float quantity, String time) {
        this.name = name;
        this.quantity = quantity;
        this.time = time;
    }
    public String getName() {
        return name;
    }

    public Float getQuantity() {
        return quantity;
    }

    public String getTime() {
        return time;
    }
}
