package com.example.myaidkit;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Medicine.class, Reminder.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MedicineDao medicineDao();
    public abstract ReminderDao reminderDao();
}
