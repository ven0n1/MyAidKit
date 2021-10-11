package com.example.myaidkit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReminderDao {
    @Query("SELECT * FROM reminder")
    List <Reminder> getAll();

    @Insert
    void insert(Reminder reminder);

    @Delete
    void delete(Reminder reminder);
}
