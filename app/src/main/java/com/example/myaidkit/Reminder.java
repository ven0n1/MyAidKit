package com.example.myaidkit;

public class Reminder {
    String name;
    String date;
    String time;

    public Reminder(String name, String date, String time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
