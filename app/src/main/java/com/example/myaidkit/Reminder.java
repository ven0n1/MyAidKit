package com.example.myaidkit;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    Float quantity;
    Long time;

    @Ignore
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
