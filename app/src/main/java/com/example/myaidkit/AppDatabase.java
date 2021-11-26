package com.example.myaidkit;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myaidkit.dao.MedicineDao;
import com.example.myaidkit.dao.ReminderDao;
import com.example.myaidkit.entity.Medicine;
import com.example.myaidkit.entity.Reminder;

@Database(entities = {Medicine.class, Reminder.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MedicineDao medicineDao();
    public abstract ReminderDao reminderDao();
}
