package com.example.myaidkit.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.myaidkit.R;
import com.example.myaidkit.Reminder;

import java.io.File;
import java.util.Objects;

public class Notification extends AppCompatActivity {
    SQLiteDatabase mydatabase;
    final String TYPE = "type";
    final String ID = "id";
    final String NAME = "name";
    final String QUANTITY = "quantity";
    final String TIME = "time";
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
        File dbpath = getApplicationContext().getDatabasePath("medicines");
        if (!Objects.requireNonNull(dbpath.getParentFile()).exists()) {
            dbpath.getParentFile().mkdirs();
        }
        mydatabase = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
        int type = getIntent().getIntExtra(TYPE, 0);
        if (type == ADD) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = editTextName.getText().toString();
                    Float quantity = Float.valueOf(editTextQuantity.getText().toString());
                    String time = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        time = t.getHour() + ":" + t.getMinute();
                    }
                    Reminder reminder = new Reminder(name, quantity, time);
                    mydatabase.execSQL("INSERT INTO Notifications(Name, Quantity, Time) VALUES(" +
                            DatabaseUtils.sqlEscapeString(reminder.getName()) + ", " +
                            reminder.getQuantity() + ", " +
                            DatabaseUtils.sqlEscapeString(reminder.getTime()) +
                            ")");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else if (type == DELETE){
            button1.setVisibility(View.VISIBLE);
            button1.setText("Удалить");
            int id = getIntent().getIntExtra(ID, 0);
            editTextName.setText(getIntent().getStringExtra(NAME));
            editTextQuantity.setText(Float.toString(getIntent().getFloatExtra(QUANTITY, 0)));
            String time = getIntent().getStringExtra(TIME);
            String[] ham = time.split(":");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                t.setHour(Integer.valueOf(ham[0]));
                t.setMinute(Integer.valueOf(ham[1]));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = editTextName.getText().toString();
                    Float quantity = Float.valueOf(editTextQuantity.getText().toString());
                    String time = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        time = t.getHour() + ":" + t.getMinute();
                    }
                    Reminder reminder = new Reminder(id, name, quantity, time);
                    ContentValues cv = new ContentValues();
                    cv.put("Name", reminder.getName());
                    cv.put("Quantity", reminder.getQuantity());
                    cv.put("Time", reminder.getTime());
                    mydatabase.update("Notifications", cv, "ID = " + reminder.getId(), null);
                    setResult(RESULT_OK);
                    finish();
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mydatabase.delete("Notifications", "ID = " + id, null);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }
}