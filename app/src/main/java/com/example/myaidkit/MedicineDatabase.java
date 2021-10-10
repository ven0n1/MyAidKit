package com.example.myaidkit;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Medicine.class}, version = 1)
public abstract class MedicineDatabase extends RoomDatabase {
    public abstract MedicineDao medicineDao();
}
