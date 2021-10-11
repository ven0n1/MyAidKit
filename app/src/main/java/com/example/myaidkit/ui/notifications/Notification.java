package com.example.myaidkit.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.example.myaidkit.AppDatabase;
import com.example.myaidkit.R;
import com.example.myaidkit.Reminder;
import com.example.myaidkit.ReminderDao;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;

public class Notification extends AppCompatActivity {
    SQLiteDatabase mydatabase;
    File dbpath;
    final String TYPE = "type";
    final String ID = "id";
    final String NAME = "name";
    final String QUANTITY = "quantity";
    final String TIME = "time";
    final String TITLE = "Пора принять лекарства";
    AppDatabase appDatabase;
    ReminderDao reminderDao;
    Reminder reminder;
    Thread thread;
    final int ADD = 1;
    final int DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Button button = findViewById(R.id.notifySave);
        Button button1 = findViewById(R.id.notifyDelete);
        EditText editTextName = findViewById(R.id.notifyName);
        EditText editTextQuantity = findViewById(R.id.notifyQuantity);
        TimePicker t = findViewById(R.id.notifyTime);
        t.setIs24HourView(true);
        final NotifyMe.Builder notifyMe = new NotifyMe.Builder(getApplicationContext());
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "medicine").build();
        reminderDao = appDatabase.reminderDao();
        int type = getIntent().getIntExtra(TYPE, 0);
        if (type == ADD) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = editTextName.getText().toString();
                    Float quantity = Float.valueOf(editTextQuantity.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        calendar.set(Calendar.HOUR_OF_DAY, t.getHour());
                        calendar.set(Calendar.MINUTE, t.getMinute());
                    }
                    reminder = new Reminder(name, quantity, calendar.getTimeInMillis());
                    thread = new Thread(){
                        @Override
                        public void run() {
                            reminderDao.insert(reminder);
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    notifyMe.title(TITLE);
                    notifyMe.content("Примите: " + reminder.getName() + " в объеме: " + reminder.getQuantity());
                    notifyMe.time(calendar);//The time to popup notification
                    notifyMe.rrule("FREQ=HOURLY");//RRULE for frequency of notification
                    notifyMe.build();
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else if (type == DELETE){
            button1.setVisibility(View.VISIBLE);
            button1.setText("Удалить");
            int id = getIntent().getIntExtra(ID, 0);
            reminder = new Reminder(id,
                    getIntent().getStringExtra(NAME),
                    getIntent().getFloatExtra(QUANTITY, 0),
                    getIntent().getLongExtra(TIME, 0));
            editTextName.setText(reminder.getName());
            editTextQuantity.setText(Float.toString(reminder.getQuantity()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reminder.getTime());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                t.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                t.setMinute(calendar.get(Calendar.MINUTE));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    thread = new Thread(){
                        @Override
                        public void run() {
                            reminderDao.delete(reminder);
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    NotifyMe.cancel(getApplicationContext(), id);
                    String name = editTextName.getText().toString();
                    Float quantity = Float.valueOf(editTextQuantity.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        calendar.set(Calendar.HOUR_OF_DAY, t.getHour());
                        calendar.set(Calendar.MINUTE, t.getMinute());
                    }
                    reminder = new Reminder(name, quantity, calendar.getTimeInMillis());
                    thread = new Thread(){
                        @Override
                        public void run() {
                            reminderDao.insert(reminder);
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    notifyMe.title(TITLE);
                    notifyMe.content("Примите: " + reminder.getName() + " в объеме: " + reminder.getQuantity());
                    notifyMe.time(calendar);//The time to popup notification
                    notifyMe.rrule("FREQ=HOURLY");//RRULE for frequency of notification
                    notifyMe.build();
                    setResult(RESULT_OK);
                    finish();
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    thread = new Thread(){
                        @Override
                        public void run() {
                            reminderDao.delete(reminder);
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    NotifyMe.cancel(getApplicationContext(), id);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }
}