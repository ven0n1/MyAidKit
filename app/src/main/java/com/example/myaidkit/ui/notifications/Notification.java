package com.example.myaidkit.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Button button = findViewById(R.id.notifySave);
        TimePicker t = findViewById(R.id.notifyTime);
        t.setIs24HourView(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txt = findViewById(R.id.notifyName);
                String name = txt.getText().toString();
                txt = findViewById(R.id.notifyQuantity);
                Float quantity = Float.valueOf(txt.getText().toString());
                String time = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    time = t.getHour() + ":" + t.getMinute();
                }
                Reminder reminder = new Reminder(name, quantity, time);
                File dbpath = getApplicationContext().getDatabasePath("medicines");
                if (!Objects.requireNonNull(dbpath.getParentFile()).exists()) {
                    dbpath.getParentFile().mkdirs();
                }
                mydatabase = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
                mydatabase.execSQL("INSERT INTO Notifications(Name, Quantity, Time) VALUES(" +
                        DatabaseUtils.sqlEscapeString(reminder.getName()) + ", " +
                        reminder.getQuantity() + ", " +
                        DatabaseUtils.sqlEscapeString(reminder.getTime()) +
                        ")");
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}