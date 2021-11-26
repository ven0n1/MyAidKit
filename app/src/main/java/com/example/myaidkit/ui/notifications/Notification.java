package com.example.myaidkit.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.allyants.notifyme.NotifyMe;
import com.example.myaidkit.AppDatabase;
import com.example.myaidkit.Constants;
import com.example.myaidkit.R;
import com.example.myaidkit.entity.Reminder;
import com.example.myaidkit.dao.ReminderDao;

import java.io.File;
import java.util.Calendar;

public class Notification extends AppCompatActivity {
    AppDatabase appDatabase;
    ReminderDao reminderDao;
    Reminder reminder;
    Thread thread;

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
        int type = getIntent().getIntExtra(Constants.TYPE, 0);
        if (type == Constants.ADD) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = editTextName.getText().toString();
                    Float quantity = editTextQuantity.getText().toString().isEmpty()?0:Float.parseFloat(editTextQuantity.getText().toString());
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
                    notifyMe.title(Constants.TITLE);
                    notifyMe.content("Примите: " + reminder.getName() + " в объеме: " + reminder.getQuantity());
                    notifyMe.time(calendar);//The time to popup notification
                    notifyMe.rrule("FREQ=HOURLY");//RRULE for frequency of notification
                    notifyMe.build();
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else if (type == Constants.DELETE){
            button1.setVisibility(View.VISIBLE);
            button1.setText("Удалить");
            int id = getIntent().getIntExtra(Constants.ID, 0);
            reminder = new Reminder(id,
                    getIntent().getStringExtra(Constants.NAME),
                    getIntent().getFloatExtra(Constants.QUANTITY, 0),
                    getIntent().getLongExtra(Constants.TIME, 0));
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
                    notifyMe.title(Constants.TITLE);
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