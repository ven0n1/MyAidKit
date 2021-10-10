package com.example.myaidkit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.List;

@Dao
public interface MedicineDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT name, link, form FROM medicine")
    List<Medicine> getAll();

    @Query("SELECT * FROM medicine where link = :link")
    Medicine getByLink(String link);

    @Insert
    void insert(Medicine medicine);

    @Delete
    void delete(Medicine medicine);
}
